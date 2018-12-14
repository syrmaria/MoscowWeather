package com.syrovama.moscowweather;

import android.app.Application;
import android.arch.persistence.room.Room;

public class MyApplication extends Application {
    private WeatherDatabase mDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        mDatabase = Room.databaseBuilder(getApplicationContext(),
                WeatherDatabase.class, "weather-database").allowMainThreadQueries().build();
    }

    public WeatherDatabase getDatabase() {
        return mDatabase;
    }
}
