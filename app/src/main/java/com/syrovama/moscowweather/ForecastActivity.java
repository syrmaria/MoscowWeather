package com.syrovama.moscowweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import static com.syrovama.moscowweather.Constants.EXTRA_ID;
import static com.syrovama.moscowweather.Constants.EXTRA_LAT;
import static com.syrovama.moscowweather.Constants.EXTRA_LON;
import static com.syrovama.moscowweather.Constants.DEFAULT_LAT;
import static com.syrovama.moscowweather.Constants.DEFAULT_LON;
import static com.syrovama.moscowweather.Constants.REQUEST_CHOOSE_LOCATION;

public class ForecastActivity extends AppCompatActivity implements ForecastPresenter.ForecastView  {
    private static final String TAG = "MyForecastActivity";
    private static ForecastPresenter mPresenter;
    private ForecastRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_list);
        if (mPresenter == null) {
            mPresenter = new ForecastPresenter(this);
        }
        initRecycler();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.attach(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.detach();
        if (isDestroyed()) {
            mPresenter.destroy();
            mPresenter = null;
        }
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
                Log.d(TAG, "Refresh button");
                mPresenter.requestWeatherData(this);
                return true;
            case R.id.choose_location_menu_item:
                openMap();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initRecycler() {
        RecyclerView recyclerView = findViewById(R.id.weather_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter = new ForecastRecyclerAdapter(mPresenter);
        mAdapter.setOnItemClickListener(new ForecastRecyclerAdapter.ClickListener() {
            @Override
            public void onItemClick(int id) {
                openDetails(id);
            }
        });
        recyclerView.setAdapter(mAdapter);
        Log.d(TAG, "Recycler created");
    }

    private void openDetails(int id) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(EXTRA_ID, id);
        startActivity(intent);
    }

    private void openMap() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, REQUEST_CHOOSE_LOCATION);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d(TAG, "onActivityResult");
        if (requestCode == REQUEST_CHOOSE_LOCATION) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "Got result from Maps");
                mPresenter.onNewLocation(this, data.getDoubleExtra(EXTRA_LAT, DEFAULT_LAT),
                        data.getDoubleExtra(EXTRA_LON, DEFAULT_LON));
            }
        }
    }

    @Override
    public void onNewForecast() {
        Log.d(TAG, "onNewForecast");
        mAdapter.notifyDataSetChanged();
    }

}
