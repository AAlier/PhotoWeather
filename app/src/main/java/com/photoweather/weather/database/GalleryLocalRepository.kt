package com.photoweather.weather.database

import com.photoweather.weather.data.Gallery
import kotlinx.coroutines.flow.Flow

interface GalleryLocalRepository {
    fun listenGallery(): Flow<List<Gallery>>
    suspend fun save(path: String)
    suspend fun delete(id: Long)
}