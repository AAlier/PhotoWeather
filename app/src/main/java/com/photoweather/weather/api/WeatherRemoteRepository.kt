package com.photoweather.weather.api

import com.photoweather.weather.data.WeatherInfo

interface WeatherRemoteRepository {
    suspend fun getWeatherAt(latitude: Double, longitude: Double): WeatherInfo
}