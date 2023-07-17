package com.photoweather.weather.data

import java.util.Calendar

data class WeatherInfo(
    val temp: Int = 0,
    val description: String = "",
    val date: Calendar,
    val country: String = "",
) {
    fun formatTemperatureCelsius(): String {
        return "${temp}°C"
    }

    fun formatDate() {

    }
}