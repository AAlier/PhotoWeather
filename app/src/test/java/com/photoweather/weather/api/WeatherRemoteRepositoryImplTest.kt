package com.photoweather.weather.api
import com.photoweather.BuildConfig
import com.photoweather.weather.api.model.Main
import com.photoweather.weather.api.model.Weather
import com.photoweather.weather.api.model.WeatherInfoResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class WeatherRemoteRepositoryImplTest {

    private lateinit var repository: WeatherRemoteRepositoryImpl
    private lateinit var weatherApi: WeatherApi

    @Before
    fun setup() {
        weatherApi = mockk(relaxed = true)
        repository = WeatherRemoteRepositoryImpl(weatherApi)
    }

    @After
    fun teardown() {
        // Clean up any mocks
    }

    @Test
    fun `getWeatherAt should return WeatherInfo`() = runTest {
        // Given
        val latitude = 37.7749
        val longitude = -122.4194
        val expectedWeatherResponse = WeatherInfoResponse(
            weather = listOf(Weather(1, "1", "2", "3")),
            base = "1",
            main = Main(10.0, 10.0, 10.0, 10.0, 101, 101),
            dt = 101,
            sys = null,
            timezone = 101,
            id = 101,
            name = "4",
            cod = 101,
        )
        coEvery {
            weatherApi.getWeatherData(
                latitude,
                longitude,
                BuildConfig.API_KEY
            )
        } returns expectedWeatherResponse

        // When
        val result = repository.getWeatherAt(latitude, longitude)

        // Then
        assertEquals(expectedWeatherResponse.weather[0].description, result.description)
    }
}