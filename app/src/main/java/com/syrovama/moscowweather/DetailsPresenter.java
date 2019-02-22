package com.syrovama.moscowweather;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

class DetailsPresenter {
    private static final String ICON_URL="https://yastatic.net/weather/i/icons/blueye/color/svg/";
    private static final String ICON_EXT=".svg";
    private DetailsView mView;

    public interface DetailsView {
        void showWeatherDetails(String date, String temp, String feels, String humidity);
        void showIcon(String icon);
    }

    public void attach(DetailsView view) {
        mView = view;
    }

    public void detach() {
        mView = null;
    }

    public void requestWeather(Context context, int id) {
        Weather weather = ((MyApplication) context.getApplicationContext())
                .getDatabase()
                .getWeatherDAO()
                .get(id);
        mView.showIcon(ICON_URL + weather.getIcon() + ICON_EXT);
        Resources resources = context.getResources();
        mView.showWeatherDetails(weather.getDate(),
                String.format(resources.getString(R.string.temp_template), weather.getTemp()),
                String.format(resources.getString(R.string.temp_template), weather.getFeels()),
                String.format(resources.getString(R.string.humidity_template), weather.getHumidity()));
    }

}
