@file:OptIn(ExperimentalCoroutinesApi::class)

package com.photoweather.weather.database

import com.photoweather.weather.data.Gallery
import com.photoweather.weather.database.model.GalleryEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import java.util.Calendar

class GalleryLocalRepositoryImpl(
    private val galleryDao: GalleryDao,
) : GalleryLocalRepository {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    override fun listenGallery(): Flow<List<Gallery>> {
        return galleryDao.listen().flowOn(dispatcher)
            .mapLatest { items -> items.map { it.toDomain() } }
    }

    override suspend fun save(path: String): Unit = withContext(dispatcher) {
        val entity = GalleryEntity(
            createdAt = Calendar.getInstance(),
            imagePath = path
        )
        galleryDao.insert(entity)
    }

    override suspend fun delete(id: Long) = withContext(dispatcher) {
        galleryDao.delete(id)
    }
}