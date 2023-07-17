package com.photoweather.weather.ui.main

import android.Manifest
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.photoweather.R
import com.photoweather.weather.data.Gallery
import com.photoweather.weather.interactor.WeatherInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel(
    weatherInteractor: WeatherInteractor
) : ViewModel() {
    val galleryFlow: Flow<List<Gallery>> = weatherInteractor.listenGallery()
    private val mState = MutableStateFlow<ViewState>(ViewState.None)
    val state: Flow<ViewState>
        get() = mState

    fun onPermissionsDenied(permission: String) {
        mState.value = when (permission) {
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                ViewState.PermissionDenied(
                    R.string.storage_permission_alert_title,
                    R.string.storage_permission_alert_message
                )
            }

            Manifest.permission.CAMERA -> {
                ViewState.PermissionDenied(
                    R.string.camera_permission_alert_title,
                    R.string.camera_permission_alert_message
                )
            }

            else -> {
                ViewState.PermissionDenied(
                    R.string.location_permission_alert_title,
                    R.string.location_permission_alert_message
                )
            }
        }
    }

    fun resetState() {
        mState.value = ViewState.None
    }

    sealed class ViewState {
        data class PermissionDenied(@StringRes val title: Int, @StringRes val message: Int) : ViewState()
        object None : ViewState()
    }
}