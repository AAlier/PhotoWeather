package com.photoweather.weather.api

import com.photoweather.BuildConfig
import com.photoweather.weather.data.WeatherInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRemoteRepositoryImpl(
    private val weatherApi: WeatherApi,
) : WeatherRemoteRepository {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    override suspend fun getWeatherAt(
        latitude: Double,
        longitude: Double
    ): WeatherInfo = withContext(dispatcher) {
        val response = weatherApi.getWeatherData(latitude, longitude, BuildConfig.API_KEY)
        return@withContext response.toDomain()
    }
}