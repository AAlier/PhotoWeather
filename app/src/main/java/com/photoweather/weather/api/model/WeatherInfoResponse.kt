package com.photoweather.weather.api.model

import com.google.gson.annotations.SerializedName

data class WeatherInfoResponse(
        @SerializedName("weather")
        val weather: List<Weather>,
        @SerializedName("base")
        val base: String,
        @SerializedName("main")
        val main: Main,
        @SerializedName("dt")
        val dt: Long,
        @SerializedName("sys")
        val sys: Sys?,
        @SerializedName("timezone")
        val timezone: Long,
        @SerializedName("id")
        val id: Long,
        @SerializedName("name")
        val name: String,
        @SerializedName("cod")
        val cod: Long
)

data class Main(
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    @SerializedName("pressure")
    val pressure: Long,
    @SerializedName("humidity")
    val humidity: Long
)

data class Sys(
    @SerializedName("type")
    val type: Long,
    @SerializedName("id")
    val id: Long,
    @SerializedName("message")
    val message: Double,
    @SerializedName("country")
    val country: String,
    @SerializedName("sunrise")
    val sunrise: Long,
    @SerializedName("sunset")
    val sunset: Long
)

data class Weather(
    @SerializedName("id")
    val id: Long,
    @SerializedName("main")
    val main: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String
)