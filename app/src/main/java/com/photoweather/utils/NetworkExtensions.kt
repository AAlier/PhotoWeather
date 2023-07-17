package com.photoweather.utils

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.TextUtils
import androidx.core.text.buildSpannedString
import com.photoweather.R
import org.apache.http.conn.ConnectTimeoutException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

fun Throwable?.getErrorMessage(context: Context): String =
    when (this) {
        is UnknownHostException,
        is TimeoutException,
        is ConnectException -> context.getString(R.string.error_network_title)
        else -> context.getString(R.string.general_error_message)
    }

private inline fun Throwable.getDescriptiveErrorMessage(newItems: (SpannableStringBuilder) -> Unit = {}) =
    buildSpannedString {
        newItems(this)
        if (!TextUtils.isEmpty(message)) {
            append("Message: ").append(message)
        }
        if (cause != null && !TextUtils.isEmpty(cause!!.stackTraceToString())) {
            append("Cause: ").append(cause!!.stackTraceToString())
        }
        if (!TextUtils.isEmpty(stackTraceToString())) {
            append("Trace: ").append(stackTraceToString())
        }
    }

fun Throwable.isNetworkError(): Boolean {
    return this is UnknownHostException ||
        this is ConnectTimeoutException ||
        this is TimeoutException ||
        this is ConnectException
}

fun Throwable.isServerError(): Boolean {
    return false // Handle Server error
}

fun Throwable.isServerTimeoutError(): Boolean {
    return this is SocketTimeoutException ||
        this is ConnectTimeoutException ||
        this is TimeoutException
}