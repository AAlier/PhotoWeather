package com.photoweather.weather.interactor

import com.photoweather.weather.api.WeatherRemoteRepository
import com.photoweather.weather.data.Gallery
import com.photoweather.weather.data.SaveImageResponse
import com.photoweather.weather.data.WeatherInfoResponse
import com.photoweather.weather.database.GalleryLocalRepository
import kotlinx.coroutines.flow.Flow

class WeatherInteractor(
    private val remoteRepository: WeatherRemoteRepository,
    private val localRepository: GalleryLocalRepository,
) {

    fun listenGallery(): Flow<List<Gallery>> {
        return localRepository.listenGallery()
    }

    suspend fun save(path: String?): SaveImageResponse {
        return try {
            if (path.isNullOrEmpty()) {
                return SaveImageResponse.Error(IllegalArgumentException("File does not exist"))
            }
            localRepository.save(path)
            SaveImageResponse.Success
        } catch (e: Exception) {
            SaveImageResponse.Error(e)
        }
    }

    suspend fun getWeather(lat: Double, log: Double): WeatherInfoResponse {
        return try {
            val info = remoteRepository.getWeatherAt(lat, log)
            WeatherInfoResponse.Success(info)
        } catch (e: Exception) {
            WeatherInfoResponse.Error(e)
        }
    }

    suspend fun delete(id: Long) {
        localRepository.delete(id)
    }
}