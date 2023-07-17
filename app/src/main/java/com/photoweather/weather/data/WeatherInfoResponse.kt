package com.photoweather.weather.data

sealed class WeatherInfoResponse {
    data class Success(val info: WeatherInfo) : WeatherInfoResponse()
    data class Error(val e: Exception) : WeatherInfoResponse()
}