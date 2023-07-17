package com.photoweather.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun formatShortTimeDate(calendar: Calendar): String {
    val sdf = SimpleDateFormat("HH:mm dd MMM", Locale.getDefault())
    return sdf.format(calendar.time)
}