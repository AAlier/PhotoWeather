package com.photoweather.weather.ui.edit

import android.location.Location
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.photoweather.weather.data.LocationInfo
import com.photoweather.weather.data.WeatherInfo
import org.junit.Rule
import org.junit.Test
import java.util.Calendar

class EditPreviewContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun editPreviewContent_DisplayedProperly() {
        val imagePath = "test_image_path"
        val locationInfo = LocationInfo(Location(""), "address")
        val weatherInfo = WeatherInfo(temp = 1, description = "1", date = Calendar.getInstance(), country = "2")

        composeTestRule.setContent {
            EditPreviewContent(
                imagePath = imagePath,
                state = EditPreviewViewModel.ViewState.None,
                location = locationInfo,
                weatherInfo = weatherInfo,
                errorMsg = null,
                onRetryClicked = {},
                onConfirmClicked = {},
                onBackPressed = {}
            )
        }

        // Verify the ImagePreview composable is displayed
        composeTestRule.onNodeWithTag("ImagePreview").assertIsDisplayed()

        // Verify the WeatherInfo composable is displayed
        composeTestRule.onNodeWithTag("WeatherInfo").assertIsDisplayed()

        // Verify the ProgressBar composable is not displayed
        composeTestRule.onNodeWithTag("ProgressBar").assertDoesNotExist()

        // Verify the ErrorPreview composable is not displayed
        composeTestRule.onNodeWithTag("ErrorPreview").assertDoesNotExist()
    }

    @Test
    fun editPreviewContent_DisplayErrorPreview() {
        val imagePath = "test_image_path"
        val errorMsg = Exception("Test error message")

        composeTestRule.setContent {
            EditPreviewContent(
                imagePath = imagePath,
                state = EditPreviewViewModel.ViewState.None,
                location = null,
                weatherInfo = null,
                errorMsg = errorMsg,
                onRetryClicked = {},
                onConfirmClicked = {},
                onBackPressed = {}
            )
        }

        // Verify the ImagePreview composable is displayed
        composeTestRule.onNodeWithTag("ImagePreview").assertIsDisplayed()

        // Verify the WeatherInfo composable is not displayed
        composeTestRule.onNodeWithTag("WeatherInfo").assertDoesNotExist()

        // Verify the ProgressBar composable is not displayed
        composeTestRule.onNodeWithTag("ProgressBar").assertDoesNotExist()

        // Verify the ErrorPreview composable is displayed
        composeTestRule.onNodeWithTag("ErrorPreview").assertIsDisplayed()
    }
}