package com.syrovama.moscowweather;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;
import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

import static com.syrovama.moscowweather.Constants.EXTRA_LAT;
import static com.syrovama.moscowweather.Constants.EXTRA_LON;
import static com.syrovama.moscowweather.Constants.DEFAULT_LAT;
import static com.syrovama.moscowweather.Constants.DEFAULT_LON;

public class WeatherService extends JobIntentService {
    public static final String TAG = "MyWeatherService";
    public static final int JOB_ID = 1;

    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, WeatherService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        double latitude = intent.getDoubleExtra(EXTRA_LAT, DEFAULT_LAT);
        double longitude = intent.getDoubleExtra(EXTRA_LON, DEFAULT_LON);
        ServiceComponent daggerServiceComponent = DaggerServiceComponent.builder().build();
        Call<List<Weather>> forecastCall = daggerServiceComponent.getWebAPI()
                .getForecast(latitude, longitude, false,false);
        List<Weather> weatherList = null;
        try {
            Response<List<Weather>> forecastResponse = forecastCall.execute();
            if (!forecastResponse.isSuccessful()) return;
            weatherList = forecastResponse.body();
            Log.d(TAG, "Data downloaded");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        if (weatherList != null) saveDataToDB(weatherList);
    }

    private void saveDataToDB(List<Weather> weatherList) {
        WeatherDAO dao = ((MyApplication)getApplication()).getDatabase().getWeatherDAO();
        dao.deleteAll();
        dao.insert(weatherList);
        Log.d(TAG, "Data saved to DB");
    }
}
