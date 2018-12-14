package com.syrovama.moscowweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class WeatherListActivity extends AppCompatActivity {
    private static final String TAG = "WeatherListActivity";
    public static final String EXTRA_DATE = "Date";
    public static final String ACTION = "com.syrovama.moscowweather.dataReady";
    private List<Weather> mWeatherList;
    private DataReadyReceiver mDataReadyReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_list);
        initRecycler();
    }

    private void initRecycler() {
        Log.d(TAG, "Create new recycler");
        mWeatherList = new ArrayList<>();
        RecyclerView mRecyclerView = findViewById(R.id.weather_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mWeatherList = ((MyApplication)getApplication()).getDatabase().getWeatherDAO().getAll();
        WeatherAdapter mAdapter = new WeatherAdapter(mWeatherList);
        mAdapter.setOnItemClickListener(new WeatherAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                openDetails(position);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void openDetails(int position) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(EXTRA_DATE, mWeatherList.get(position).getDate());
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_menu_item:
                getWeatherData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getWeatherData() {
        Intent intent = new Intent(this, WeatherService.class);
        WeatherService.enqueueWork(this, intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDataReadyReceiver = new DataReadyReceiver(new Handler());
        IntentFilter intentFilter = new IntentFilter(ACTION);
        registerReceiver(mDataReadyReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        unregisterReceiver(mDataReadyReceiver);
        super.onPause();
    }

    public class DataReadyReceiver extends BroadcastReceiver {
        private final Handler handler;

        public DataReadyReceiver(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    initRecycler();
                }
            });
        }
    }

}
