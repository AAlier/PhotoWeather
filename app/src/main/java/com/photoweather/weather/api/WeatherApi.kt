package com.photoweather.weather.api

import com.photoweather.weather.api.model.WeatherInfoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getWeatherData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") api: String,
    ): WeatherInfoResponse
}