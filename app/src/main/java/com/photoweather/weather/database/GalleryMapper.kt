package com.photoweather.weather.database

import com.photoweather.weather.data.Gallery
import com.photoweather.weather.database.model.GalleryEntity

fun GalleryEntity.toDomain(): Gallery {
    return Gallery(
        id = id,
        imagePath = imagePath,
        createdAt = createdAt
    )
}