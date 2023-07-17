package com.photoweather.weather.data

import android.os.Parcel
import android.os.Parcelable
import java.io.File
import java.util.Calendar

data class Gallery(
    val id: Long,
    val imagePath: String,
    val createdAt: Calendar = Calendar.getInstance()
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        Calendar.getInstance().apply {
            timeInMillis = parcel.readLong()
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(imagePath)
        parcel.writeLong(createdAt.timeInMillis)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Gallery> {
        override fun createFromParcel(parcel: Parcel): Gallery {
            return Gallery(parcel)
        }

        override fun newArray(size: Int): Array<Gallery?> {
            return arrayOfNulls(size)
        }
    }

    fun toFile(): File {
        return File(imagePath)
    }
}