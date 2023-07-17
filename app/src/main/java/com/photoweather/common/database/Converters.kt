package com.photoweather.common.database

import androidx.room.TypeConverter
import java.util.Calendar

class Converters {
    @TypeConverter
    fun toDate(dateLong: Long?): Calendar? {
        return dateLong?.let {
            Calendar.getInstance().apply { timeInMillis = it }
        }
    }

    @TypeConverter
    fun fromDate(date: Calendar?): Long? {
        return date?.timeInMillis
    }
}