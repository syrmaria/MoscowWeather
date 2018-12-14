package com.syrovama.moscowweather;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface WebAPI {
    @Headers("X-Yandex-API-Key: 0322acf3-65a0-441a-8832-254fc940b9a8")
    @GET("/v1/forecast")
    Call<List<Weather>> getForecast(@Query("lat") String latitude,
                                    @Query("lon") String longitude,
                                    @Query("hours") Boolean needHours,
                                    @Query("extra") Boolean needExtra );
}
