package com.photoweather.weather.api

import com.photoweather.weather.api.model.WeatherInfoResponse
import com.photoweather.weather.data.WeatherInfo
import java.util.Calendar

fun WeatherInfoResponse.toDomain(): WeatherInfo {
    return WeatherInfo(
        country = sys?.country ?: "Country",
        temp = main.temp.toInt(),
        description = weather[0].description,
        date = Calendar.getInstance(),
    )
}