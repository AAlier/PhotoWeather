package com.photoweather.weather.ui.edit

import android.app.Activity
import android.graphics.Bitmap
import android.location.Location
import android.view.View
import android.view.Window
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.photoweather.common.location.LocationManager
import com.photoweather.utils.ViewCapture
import com.photoweather.weather.data.LocationInfo
import com.photoweather.weather.data.SaveImageResponse
import com.photoweather.weather.data.WeatherInfo
import com.photoweather.weather.data.WeatherInfoResponse
import com.photoweather.weather.interactor.WeatherInteractor
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

@ExperimentalCoroutinesApi
class EditPreviewViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: EditPreviewViewModel
    private lateinit var locationManager: LocationManager
    private lateinit var weatherInteractor: WeatherInteractor
    private lateinit var viewCapture: ViewCapture

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        locationManager = mockk(relaxed = true)
        weatherInteractor = mockk(relaxed = true)
        viewCapture = mockk(relaxed = true)

        viewModel = EditPreviewViewModel(locationManager, weatherInteractor, viewCapture)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `startLocationUpdate should call locationManager to start location updates`() {
        val activity = mockk<Activity>(relaxed = true)

        viewModel.startLocationUpdate(activity)

        verify { locationManager.startLocationUpdates(activity) }
    }

    @Test
    fun `stopLocationUpdate should call locationManager to stop location updates`() {
        viewModel.stopLocationUpdate()

        verify { locationManager.stopLocationUpdates() }
    }

    @Test
    fun `getWeatherInfo should update weatherInfo when weather data is successfully retrieved`() {
        val location = mockk<Location>(relaxed = true)
        val weatherInfo = mockk<WeatherInfo>(relaxed = true)
        val response = WeatherInfoResponse.Success(weatherInfo)

        coEvery { weatherInteractor.getWeather(any(), any()) } returns response

        viewModel.getWeatherInfo(location)

        assert(viewModel.weatherInfo.value == weatherInfo)
        assert(viewModel.error.value == null)

        coVerify { weatherInteractor.getWeather(location.latitude, location.longitude) }
    }

    @Test
    fun `getWeatherInfo should update error when weather data retrieval fails`() {
        val location = mockk<Location>(relaxed = true)
        val exception = mockk<Exception>(relaxed = true)
        val response = WeatherInfoResponse.Error(exception)

        coEvery { weatherInteractor.getWeather(any(), any()) } returns response

        viewModel.getWeatherInfo(location)

        assert(viewModel.weatherInfo.value == null)
        assert(viewModel.error.value == exception)

        coVerify { weatherInteractor.getWeather(location.latitude, location.longitude) }
    }

    @Test
    fun `onRetry should call getWeatherInfo when locationInfo is not null`() {
        val activity = mockk<Activity>(relaxed = true)
        val locationInfo = LocationInfo(mockk(relaxed = true), "Address")
        viewModel.locationInfo.value = locationInfo

        viewModel.onRetry(activity)

        verify { viewModel.getWeatherInfo(locationInfo.location) }
    }

    @Test
    fun `save should update state to Saving and call weatherInteractor to save image`() {
        val file = mockk<File>(relaxed = true)
        val view = mockk<View>(relaxed = true)
        val window = mockk<Window>(relaxed = true)
        val bitmap = mockk<Bitmap>(relaxed = true)
        val response = SaveImageResponse.Success

        coEvery { viewCapture.captureView(view, window) } returns bitmap
        coEvery { weatherInteractor.save(file.path) } returns response

        viewModel.save(file, view, window)

        assert(viewModel.state.value == EditPreviewViewModel.ViewState.Saving)
    }
}
