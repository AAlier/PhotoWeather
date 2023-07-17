package com.photoweather.weather.ui.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photoweather.weather.interactor.WeatherInteractor
import kotlinx.coroutines.launch

class PreviewViewModel(
    private val weatherInteractor: WeatherInteractor
): ViewModel() {

    fun onDelete(id: Long) {
        viewModelScope.launch {
            weatherInteractor.delete(id)
        }
    }
}