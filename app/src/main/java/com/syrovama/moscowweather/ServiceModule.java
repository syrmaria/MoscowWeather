package com.syrovama.moscowweather;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ServiceModule {
    private static final String BASE_URL = "https://api.weather.yandex.ru";

    @ServiceScope
    @Provides
    public WebAPI webApi(Retrofit retrofit){
        return retrofit.create(WebAPI.class);
    }

    @ServiceScope
    @Provides
    public Retrofit retrofit(GsonConverterFactory gsonConverterFactory, Gson gson){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

    @ServiceScope
    @Provides
    public Gson gson(){
        Type myListType = new TypeToken<List<Weather>>() {}.getType();
        return new GsonBuilder()
                .registerTypeAdapter(myListType, new ForecastTypeAdapter())
                .create();
    }

    @ServiceScope
    @Provides
    public GsonConverterFactory gsonConverterFactory(Gson gson){
        return GsonConverterFactory.create(gson);
    }
}


