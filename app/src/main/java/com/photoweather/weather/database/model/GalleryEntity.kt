package com.photoweather.weather.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.photoweather.weather.database.model.GalleryEntity.Companion.TABLE_NAME
import java.util.Calendar

@Entity(tableName = TABLE_NAME)
data class GalleryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Long = 0,
    @ColumnInfo(name = COLUMN_CREATED_AT)
    val createdAt: Calendar,
    @ColumnInfo(name = COLUMN_IMAGE_PATH)
    val imagePath: String
) {
    companion object {
        const val TABLE_NAME = "gallery"
        const val COLUMN_ID = "id"
        const val COLUMN_CREATED_AT = "created_at"
        const val COLUMN_IMAGE_PATH = "imagePath"
    }
}