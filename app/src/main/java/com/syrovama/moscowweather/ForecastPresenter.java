package com.syrovama.moscowweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.syrovama.moscowweather.Constants.DEFAULT_LAT;
import static com.syrovama.moscowweather.Constants.DEFAULT_LON;
import static com.syrovama.moscowweather.Constants.EXTRA_LAT;
import static com.syrovama.moscowweather.Constants.EXTRA_LON;

public class ForecastPresenter {
    private static final String TAG = "MyMainPresenter";
    private List<Weather> mWeatherList;
    private Disposable mSubscription;
    private Consumer<List<Weather>> mConsumer;
    private double mLatitude, mLongitude;
    private ForecastView mView;

    public interface ForecastView {
        void onNewForecast();
    }

    public interface ForecastRowView {
        void setWeather(String date, String temp);
    }

    ForecastPresenter(Context context) {
        Log.d(TAG, "Presenter created");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        mLatitude = Double.parseDouble(preferences.getString(EXTRA_LAT, String.valueOf(DEFAULT_LAT)));
        mLongitude = Double.parseDouble(preferences.getString(EXTRA_LON, String.valueOf(DEFAULT_LON)));
        createDataSubscription(context);
    }

    public void onBindRowViewAtPosition(int position, ForecastRowView rowView) {
        Weather weather = mWeatherList.get(position);
        String temp = String.format("%s°C", weather.getTemp());
        rowView.setWeather(weather.getDate(), temp);
    }

    public int getRowsCount() {
        if (mWeatherList == null) return 0;
        else return mWeatherList.size();
    }

    public int getId(int position) {
        return mWeatherList.get(position).getId();
    }

    public void attach(ForecastView view) {
        Log.d(TAG, "ForecastView attached");
        mView = view;
    }

    public void detach() {
        Log.d(TAG, "ForecastView detached");
        mView = null;
    }

    public void destroy() {
        if (!mSubscription.isDisposed()) mSubscription.dispose();
        mSubscription = null;
        mConsumer = null;
        Log.d(TAG, "ForecastPresenter destroyed");
    }

    private void createDataSubscription(final Context context) {
        mConsumer = new Consumer<List<Weather>>() {
            @Override
            public void accept(List<Weather> weatherData) {
                if ((weatherData == null)||(weatherData.size() == 0)) {
                    Log.d(TAG, "Consumer got empty list");
                    requestWeatherData(context);
                } else {
                    Log.d(TAG, "Consumer got message. List size = " + weatherData.size());
                    mWeatherList = weatherData;
                    updateView();
                }
            }
        };
        mSubscription = ((MyApplication) context.getApplicationContext())
                .getDatabase()
                .getWeatherDAO()
                .getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mConsumer);
        Log.d(TAG, "Subscription created");
    }


    private void updateView() {
        mView.onNewForecast();
    }

    public void onNewLocation(Context context, double lat, double lon) {
        if ((lat == mLatitude) && (lon == mLongitude)) return;
        mLatitude = lat;
        mLongitude = lon;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit()
                .putString(EXTRA_LAT, String.valueOf(lat)).putString(EXTRA_LON, String.valueOf(lon))
                .apply();
        Log.d(TAG, "Location saved to preferences");
        requestWeatherData(context);
    }

    public void requestWeatherData(Context context) {
        Log.d(TAG, "Service call");
        Intent intent = new Intent(context, WeatherService.class);
        intent.putExtra(EXTRA_LAT, mLatitude);
        intent.putExtra(EXTRA_LON, mLongitude);
        WeatherService.enqueueWork(context, intent);
    }
}
