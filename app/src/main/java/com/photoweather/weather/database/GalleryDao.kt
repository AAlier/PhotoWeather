package com.photoweather.weather.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.photoweather.weather.database.model.GalleryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GalleryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: GalleryEntity)

    @Query("""SELECT * FROM ${GalleryEntity.TABLE_NAME} ORDER BY ${GalleryEntity.COLUMN_CREATED_AT} ASC""")
    fun listen(): Flow<List<GalleryEntity>>

    @Query("DELETE FROM ${GalleryEntity.TABLE_NAME} WHERE ${GalleryEntity.COLUMN_ID} = :id")
    suspend fun delete(id: Long)
}