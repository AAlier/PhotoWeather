package com.photoweather.weather.data

sealed class SaveImageResponse {
    object Success : SaveImageResponse()
    data class Error(val e: Exception) : SaveImageResponse()
}