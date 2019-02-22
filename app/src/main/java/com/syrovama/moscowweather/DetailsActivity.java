package com.syrovama.moscowweather;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadrosid.svgloader.SvgLoader;

import static com.syrovama.moscowweather.Constants.EXTRA_ID;

public class DetailsActivity extends Activity implements DetailsPresenter.DetailsView {
    private TextView mDateTextView;
    private TextView mTempTextView;
    private TextView mFeelsTextView;
    private TextView mHumidityTextView;
    private DetailsPresenter mPresenter;
    private ImageView mIconImageView;
    private int mId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initViews();
        if (mPresenter == null) mPresenter = new DetailsPresenter();
        mId = getIntent().getIntExtra(EXTRA_ID, 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.attach(this);
        mPresenter.requestWeather(this, mId);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.detach();
        if (isDestroyed()) {
            mPresenter = null;
            SvgLoader.pluck().close();
        }
    }

    private void initViews() {
        mDateTextView = findViewById(R.id.details_date_text);
        mTempTextView = findViewById(R.id.details_temp_text);
        mFeelsTextView = findViewById(R.id.details_feels_text);
        mHumidityTextView = findViewById(R.id.details_humidity_text);
        mIconImageView = findViewById(R.id.icon);
    }


    @Override
    public void showWeatherDetails(String date, String temp, String feels, String humidity) {
        mDateTextView.setText(date);
        mTempTextView.setText(temp);
        mFeelsTextView.setText(feels);
        mHumidityTextView.setText(humidity);
    }

    @Override
    public void showIcon(String url) {
        SvgLoader.pluck().with(this).load(url, mIconImageView);
    }
}
