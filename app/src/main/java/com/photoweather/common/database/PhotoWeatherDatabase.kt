package com.photoweather.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.photoweather.common.database.PhotoWeatherDatabase.Companion.DATABASE_VERSION
import com.photoweather.weather.database.GalleryDao
import com.photoweather.weather.database.model.GalleryEntity

@Database(
    entities = [
        GalleryEntity::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PhotoWeatherDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "weather"
        const val DATABASE_VERSION = 1
    }

    abstract fun galleryDao(): GalleryDao
}