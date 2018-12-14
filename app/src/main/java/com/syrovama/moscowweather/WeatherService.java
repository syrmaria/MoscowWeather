package com.syrovama.moscowweather;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherService extends JobIntentService {
    public static final String TAG = "WeatherService";
    public static final int JOB_ID = 1;
    public static final String BASE_URL = "https://api.weather.yandex.ru";
    public static final String LAT = "55.75396";
    public static final String LON = "37.62039";

    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, WeatherService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(getGsonConverter())
                .build();
        WebAPI webAPI = retrofit.create(WebAPI.class);
        Call<List<Weather>> forecastCall = webAPI.getForecast(LAT, LON, false,false);
        List<Weather> weatherList = null;
        try {
            Response<List<Weather>> forecastResponse = forecastCall.execute();
            if (!forecastResponse.isSuccessful()) return;
            weatherList = forecastResponse.body();
            Log.d(TAG, "Data downloaded");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (weatherList != null) saveDataToDB(weatherList);
    }

    private void saveDataToDB(List<Weather> weatherList) {
        WeatherDAO dao = ((MyApplication)getApplication()).getDatabase().getWeatherDAO();
        dao.deleteAll();
        dao.insert(weatherList);
        Log.d(TAG, "Data saved to DB");
        Intent broadcastIntent = new Intent(WeatherListActivity.ACTION);
        sendBroadcast(broadcastIntent);
        Log.d(TAG, "Broadcast sent");
    }

    public GsonConverterFactory getGsonConverter() {
        Type myListType = new TypeToken<List<Weather>>() {}.getType();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(myListType, new ForecastTypeAdapter())
                .create();
        return GsonConverterFactory.create(gson);
    }

    public class ForecastTypeAdapter implements JsonDeserializer<List<Weather>> {
        public static final String TAG = "ForecastTypeAdapter";
        @Override
        public List<Weather> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            final JsonObject jsonObject = json.getAsJsonObject();
            final JsonArray jsonForecastsArray = jsonObject.get("forecasts").getAsJsonArray();
            List<Weather> weatherList = new ArrayList<>();
            for (int i = 0; i < jsonForecastsArray.size(); i++) {
                Weather weather = new Weather();
                final JsonObject forecastObject = jsonForecastsArray.get(i).getAsJsonObject();
                String date = forecastObject.get("date").getAsString();
                Log.d(TAG, "date = " + date);
                weather.setDate(date);
                final JsonObject partsObject = forecastObject.get("parts").getAsJsonObject();
                final JsonObject dayShortObject = partsObject.get("day_short").getAsJsonObject();
                Log.d("ForecastTypeAdapter", dayShortObject.toString());
                String temp = dayShortObject.get("temp").getAsString();
                weather.setTemp(temp);
                String feels = dayShortObject.get("feels_like").getAsString();
                weather.setFeels(feels);
                String condition = dayShortObject.get("condition").getAsString();
                weather.setCondition(condition);
                String humidity = dayShortObject.get("humidity").getAsString();
                weather.setHumidity(humidity);
                String icon = dayShortObject.get("icon").getAsString();
                weather.setIcon(icon);
                weatherList.add(weather);
            }
            return weatherList;
        }

    }
}
