package com.photoweather.weather.ui.main

import android.Manifest
import com.photoweather.R
import com.photoweather.weather.data.Gallery
import com.photoweather.weather.interactor.WeatherInteractor
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private lateinit var weatherInteractor: WeatherInteractor

    @Before
    fun setup() {
        weatherInteractor = mockk(relaxed = true)
        viewModel = MainViewModel(weatherInteractor)
    }

    @After
    fun teardown() {
        // Clean up any mocks
    }

    @Test
    fun `onPermissionsDenied with WRITE_EXTERNAL_STORAGE permission should update state with appropriate message`() = runTest {
            // Given
            val expectedTitle = R.string.storage_permission_alert_title
            val expectedMessage = R.string.storage_permission_alert_message

            // When
            viewModel.onPermissionsDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)

            val stateValue = viewModel.state.first()
            // Then
            assertTrue(stateValue is MainViewModel.ViewState.PermissionDenied)
            stateValue as MainViewModel.ViewState.PermissionDenied
            assertEquals(expectedTitle, stateValue.title)
            assertEquals(expectedMessage, stateValue.message)
        }

    @Test
    fun `onPermissionsDenied with CAMERA permission should update state with appropriate message`() = runBlocking {
        // Given
        val expectedTitle = R.string.camera_permission_alert_title
        val expectedMessage = R.string.camera_permission_alert_message

        // When
        viewModel.onPermissionsDenied(Manifest.permission.CAMERA)

        val stateValue = viewModel.state.first()
        // Then
        assertTrue(stateValue is MainViewModel.ViewState.PermissionDenied)
        stateValue as MainViewModel.ViewState.PermissionDenied
        assertEquals(expectedTitle, stateValue.title)
        assertEquals(expectedMessage, stateValue.message)
    }

    @Test
    fun `onPermissionsDenied with unknown permission should update state with default message`() = runBlocking {
        // Given
        val expectedTitle = R.string.location_permission_alert_title
        val expectedMessage = R.string.location_permission_alert_message

        // When
        viewModel.onPermissionsDenied("unknown_permission")

        val stateValue = viewModel.state.first()
        // Then
        assertTrue(stateValue is MainViewModel.ViewState.PermissionDenied)
        stateValue as MainViewModel.ViewState.PermissionDenied
        assertEquals(expectedTitle, stateValue.title)
        assertEquals(expectedMessage, stateValue.message)
    }

    @Test
    fun `resetState should update state to None`() = runTest {
        // Given
        viewModel.onPermissionsDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        // When
        viewModel.resetState()

        // Then
        val stateValue = viewModel.state.first()
        assertTrue(stateValue is MainViewModel.ViewState.None)
    }

    @Test
    fun `galleryFlow should emit expected list of galleries`() = runTest {
        // Given
        val expectedGalleries = listOf(
            Gallery(id = 1, imagePath = "path1"),
            Gallery(id = 2, imagePath = "path2")
        )
        coEvery { weatherInteractor.listenGallery() } returns flowOf(expectedGalleries)

        // When
        val flow = viewModel.galleryFlow
        // Then
        flow.collect { galleries ->
            galleries.forEachIndexed { index, gallery ->
                assert(gallery == expectedGalleries[index])
            }
        }
    }
}
