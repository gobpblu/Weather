package com.company.api

import com.company.model.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    fun getWeatherData(
        @Query("q") cityName: String,
    @Query("appid") appId: String,
    @Query("units") units: String,
    @Query("lang") lang: String): Call<WeatherData>
}