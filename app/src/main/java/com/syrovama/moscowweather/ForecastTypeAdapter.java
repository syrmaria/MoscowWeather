package com.syrovama.moscowweather;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ForecastTypeAdapter implements JsonDeserializer<List<Weather>> {
    static final String TAG = "ForecastTypeAdapter";

    @Override
    public List<Weather> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final JsonArray jsonForecastsArray = jsonObject.get("forecasts").getAsJsonArray();
        List<Weather> weatherList = new ArrayList<>();
        for (int i = 0; i < jsonForecastsArray.size(); i++) {
            Weather weather = new Weather();
            final JsonObject forecastObject = jsonForecastsArray.get(i).getAsJsonObject();
            String date = forecastObject.get("date").getAsString();
            //Log.d(TAG, "date = " + date);
            weather.setDate(date);
            final JsonObject partsObject = forecastObject.get("parts").getAsJsonObject();
            final JsonObject dayShortObject = partsObject.get("day_short").getAsJsonObject();
            //Log.d("ForecastTypeAdapter", dayShortObject.toString());
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
