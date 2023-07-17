package com.photoweather.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.PixelCopy
import android.view.View
import android.view.Window
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface ViewCapture {
    suspend fun captureView(view: View, window: Window): Bitmap
}

class PixelCopyViewCapture : ViewCapture {
    override suspend fun captureView(view: View, window: Window) = suspendCoroutine<Bitmap> { cont ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val actionBarHeight = getActionBarHeight(view)
            // Above Android O, use PixelCopy
            val bitmap = Bitmap.createBitmap(view.width, view.height - actionBarHeight, Bitmap.Config.ARGB_8888)
            val location = IntArray(2)
            view.getLocationInWindow(location)
            val bounds = Rect(
                location[0],
                location[1] + actionBarHeight,
                location[0] + view.width,
                location[1] + view.height
            )
            PixelCopy.request(
                window, bounds, bitmap, {
                    if (it == PixelCopy.SUCCESS) {
                        cont.resume(bitmap)
                    } else {
                        cont.resumeWithException(IllegalStateException("Failed to obtain bitmap"))
                    }
                },
                Handler(Looper.getMainLooper())
            )
        } else {
            val tBitmap = Bitmap.createBitmap(
                view.width, view.height, Bitmap.Config.RGB_565
            )
            val canvas = Canvas(tBitmap)
            view.draw(canvas)
            canvas.setBitmap(null)
            cont.resume(tBitmap)
        }
    }

    private fun getActionBarHeight(view: View): Int {
        val tv = TypedValue()
        if (view.context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, view.resources.displayMetrics)
        }
        return 0
    }
}