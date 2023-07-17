package com.photoweather.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
}

fun delete(path: String): Boolean {
    val file = File(path)
    return if (file.isFile && file.exists()) {
        file.delete()
    } else
        false
}

fun getMediaFolder(context: Context, childFolder: String = "photos"): File {
    val parentContext = context.applicationContext
    val storageDirectory = parentContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val appFolder = File(storageDirectory, childFolder)

    val wasCreated = appFolder.mkdirs()
    if (!wasCreated) {
        Timber.e("Failed to create directory")
    }
    return appFolder
}

fun replaceOriginalBitmapWithGeneratedBitmap(path: File, capturedImageBitmap: Bitmap) {
    val newBitmap = capturedImageBitmap.copy(Bitmap.Config.ARGB_8888, true)
    var outputStream: FileOutputStream? = null
    try {
        outputStream = FileOutputStream(path)
        newBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    } catch (e: FileNotFoundException) {
        Timber.e(e)
    } finally {
        if (outputStream != null) {
            try {
                outputStream.flush()
                outputStream.fd.sync()
                outputStream.close()
            } catch (e: IOException) {
                Timber.e(e)
            }
        }
    }
}

fun Context.share(type: String = "image/*", file: File) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = type
    val uri = FileProvider.getUriForFile(this, "$packageName.provider", file)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    startActivity(Intent.createChooser(intent, "Share with..."))
}

fun Context.isPermissionGranted(permission: String): Boolean {
    return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, permission)
}
