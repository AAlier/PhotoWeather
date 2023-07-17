package com.photoweather.weather.interactor

import com.photoweather.weather.api.WeatherRemoteRepository
import com.photoweather.weather.data.Gallery
import com.photoweather.weather.data.SaveImageResponse
import com.photoweather.weather.data.WeatherInfo
import com.photoweather.weather.data.WeatherInfoResponse
import com.photoweather.weather.database.GalleryLocalRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.util.Calendar

@ExperimentalCoroutinesApi
class WeatherInteractorTest {

    private lateinit var interactor: WeatherInteractor
    private lateinit var remoteRepository: WeatherRemoteRepository
    private lateinit var localRepository: GalleryLocalRepository

    @Before
    fun setup() {
        remoteRepository = mockk(relaxed = true)
        localRepository = mockk(relaxed = true)
        interactor = WeatherInteractor(remoteRepository, localRepository)
    }

    @After
    fun teardown() {
        // Clean up any mocks
    }

    @Test
    fun `listenGallery should return flow of galleries from local repository`() = runTest {
        // Given
        val expectedGalleries = listOf(
            Gallery(id = 1, imagePath = "path1", createdAt = Calendar.getInstance()),
            Gallery(id = 2, imagePath = "path2", createdAt = Calendar.getInstance())
        )
        coEvery { localRepository.listenGallery() } returns flowOf(expectedGalleries)

        // When
        val result = interactor.listenGallery()
        result.collect { galleries ->
            galleries.forEachIndexed { index, gallery ->
                assert(gallery == expectedGalleries[index])
            }
        }
    }

    @Test
    fun `save with valid path should return SaveImageResponse Success`() = runTest {
        // Given
        val path = "valid_path"

        // When
        val result = interactor.save(path)

        // Then
        assertTrue(result is SaveImageResponse.Success)
    }

    @Test
    fun `save with empty path should return SaveImageResponse Error`() = runTest {
        // Given
        val path = ""

        // When
        val result = interactor.save(path)

        // Then
        assertTrue(result is SaveImageResponse.Error)
    }

    @Test
    fun `getWeather with successful API response should return WeatherInfoResponse Success`() = runTest {
        // Given
        val lat = 37.7749
        val lon = -122.4194
        val weatherInfo = WeatherInfo(
            temp = 16,
            description = "Some description",
            date = Calendar.getInstance(),
            country = "KG"
        )
        coEvery { remoteRepository.getWeatherAt(lat, lon) } returns weatherInfo

        // When
        val result = interactor.getWeather(lat, lon)

        // Then
        assertTrue(result is WeatherInfoResponse.Success)
        assertEquals(weatherInfo, (result as WeatherInfoResponse.Success).info)
    }

    @Test
    fun `getWeather with API exception should return WeatherInfoResponse Error`() = runTest {
        // Given
        val lat = 37.7749
        val lon = -122.4194
        val exception = IOException("API exception")
        coEvery { remoteRepository.getWeatherAt(lat, lon) } throws exception

        // When
        val result = interactor.getWeather(lat, lon)

        // Then
        assertTrue(result is WeatherInfoResponse.Error)
        assertEquals(exception, (result as WeatherInfoResponse.Error).e)
    }

    @Test
    fun `delete should call delete function in local repository`() = runTest {
        // Given
        val id = 1L

        // When
        interactor.delete(id)

        // Then
        coVerify { localRepository.delete(id) }
    }
}