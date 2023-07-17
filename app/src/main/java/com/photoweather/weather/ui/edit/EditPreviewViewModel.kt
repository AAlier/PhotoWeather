package com.photoweather.weather.ui.edit

import android.app.Activity
import android.location.Location
import android.view.View
import android.view.Window
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photoweather.common.location.LocationManager
import com.photoweather.utils.ViewCapture
import com.photoweather.utils.replaceOriginalBitmapWithGeneratedBitmap
import com.photoweather.weather.data.LocationInfo
import com.photoweather.weather.data.SaveImageResponse
import com.photoweather.weather.data.WeatherInfo
import com.photoweather.weather.data.WeatherInfoResponse
import com.photoweather.weather.interactor.WeatherInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class EditPreviewViewModel(
    private val locationManager: LocationManager,
    private val weatherInteractor: WeatherInteractor,
    private val viewCapture: ViewCapture
) : ViewModel() {

    val locationInfo = MutableStateFlow<LocationInfo?>(null)
    val weatherInfo = MutableStateFlow<WeatherInfo?>(null)
    val error = MutableStateFlow<Exception?>(null)
    val mState = MutableStateFlow<ViewState>(ViewState.None)
    val state: StateFlow<ViewState>
        get() = mState

    init {
        locationManager.onLocationChanged = { location, address ->
            locationInfo.value = LocationInfo(location, address)
            getWeatherInfo(location)
        }
    }

    fun startLocationUpdate(activity: Activity) {
        viewModelScope.launch(Dispatchers.IO) {
            locationManager.startLocationUpdates(activity)
        }
    }

    fun stopLocationUpdate() {
        locationManager.stopLocationUpdates()
    }

    @VisibleForTesting
    fun getWeatherInfo(location: Location?) {
        if (location == null) {
            Timber.e("Error fetching weather for unknown location")
            return
        }
        viewModelScope.launch {
            when (val value = weatherInteractor.getWeather(location.latitude, location.longitude)) {
                is WeatherInfoResponse.Success -> weatherInfo.value = value.info
                is WeatherInfoResponse.Error -> error.value = value.e
            }
        }
    }

    fun onRetry(activity: Activity) {
        if (locationInfo.value == null) startLocationUpdate(activity)
        else getWeatherInfo(location = locationInfo.value!!.location)
    }

    fun save(file: File, view: View, window: Window) {
        mState.value = ViewState.Saving
        viewModelScope.launch(Dispatchers.IO) {
            val bitmap = viewCapture.captureView(view, window)
            replaceOriginalBitmapWithGeneratedBitmap(file, bitmap)
            when (val value = weatherInteractor.save(file.path)) {
                SaveImageResponse.Success -> {
                    mState.value = ViewState.Saved
                    Timber.i("Image saved success")
                }
                is SaveImageResponse.Error -> {
                    mState.value = ViewState.None
                    error.value = value.e
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdate()
    }

    sealed class ViewState {
        object Saving : ViewState()
        object Saved : ViewState()
        object None : ViewState()
    }
}