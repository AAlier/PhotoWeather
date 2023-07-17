package com.photoweather.weather.ui.preview
import com.photoweather.weather.interactor.WeatherInteractor
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PreviewViewModelTest {

    private lateinit var viewModel: PreviewViewModel
    private lateinit var weatherInteractor: WeatherInteractor
    private lateinit var testDispatcher: TestCoroutineDispatcher

    @Before
    fun setup() {
        weatherInteractor = mockk(relaxed = true)
        viewModel = PreviewViewModel(weatherInteractor)
        testDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun teardown() {
        // Clean up any mocks and reset Dispatchers.Main
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }


    @Test
    fun `onDelete should call WeatherInteractor delete method`() = testDispatcher.runBlockingTest {
        // Given
        val id = 123L

        // When
        viewModel.onDelete(id)

        // Then
        coVerify { weatherInteractor.delete(id) }
    }
}