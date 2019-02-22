package com.syrovama.moscowweather;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface WebAPI {
    @Headers("X-Yandex-API-Key: a01d3a42-7d16-4676-a52a-6b33efaf6cfa")
    @GET("/v1/forecast")
    Call<List<Weather>> getForecast(@Query("lat") double latitude,
                                    @Query("lon") double longitude,
                                    @Query("hours") boolean needHours,
                                    @Query("extra") boolean needExtra);
}
