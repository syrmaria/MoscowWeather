package com.syrovama.moscowweather;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

public class DetailsActivity extends Activity {
    private TextView mDateTextView;
    private TextView mTempTextView;
    private TextView mFeelsTextView;
    private TextView mHumidityTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initViews();
        setWeather();
    }

    private void setWeather() {
        String date = getIntent().getStringExtra(WeatherListActivity.EXTRA_DATE);
        Weather weather = ((MyApplication)getApplication()).getDatabase().getWeatherDAO().get(date);
        mDateTextView.setText(weather.getDate());
        mTempTextView.setText(getString(R.string.temp_template, weather.getTemp()));
        mFeelsTextView.setText(getString(R.string.temp_template, weather.getFeels()));
        //humidity_template is formatted string which itself contains symbol %
        mHumidityTextView.setText(getString(R.string.humidity_template, weather.getHumidity()));
    }

    private void initViews() {
        mDateTextView = findViewById(R.id.details_date_text);
        mTempTextView = findViewById(R.id.details_temp_text);
        mFeelsTextView = findViewById(R.id.details_feels_text);
        mHumidityTextView = findViewById(R.id.details_humidity_text);
    }


}
