package com.photoweather.weather.ui.preview

import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import org.junit.Rule
import org.junit.Test

class PreviewContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testPreviewContent_ElementsDisplayed() {
        val imagePath = "test_image_path"

        composeTestRule.setContent {
            PreviewContent(
                imagePath = imagePath,
                onShareClicked = {},
                onDeleteClicked = {},
                onBackPressed = {}
            )
        }

        // Assert the presence of top app bar and its title
        composeTestRule.onNodeWithContentDescription("Back").assertExists()
        composeTestRule.onNodeWithTag("Preview").assertExists()

        // Assert the presence of image preview
        composeTestRule.onNodeWithContentDescription("PhotoWeather").assertExists()

        // Assert the presence of footer row
        composeTestRule.onNodeWithTag("Footer").assertExists()

        // Assert the presence of share and delete buttons in the footer
        composeTestRule.onNodeWithContentDescription("Share").assertExists()
        composeTestRule.onNodeWithContentDescription("Delete").assertExists()
    }

    @Test
    fun testPreviewContent_ElementsSize() {
        val imagePath = "test_image_path"

        composeTestRule.setContent {
            PreviewContent(
                imagePath = imagePath,
                onShareClicked = {},
                onDeleteClicked = {},
                onBackPressed = {}
            )
        }

        // Assert the size of the image preview
        composeTestRule.onNodeWithContentDescription("PhotoWeather").assertWidthIsAtLeast(0.dp)

        // Assert the size of the footer row
        composeTestRule.onNodeWithTag("Footer").assertWidthIsAtLeast(0.dp)
    }

    @Test
    fun testPreviewContent_ButtonsClick() {
        var shareClicked = false
        var deleteClicked = false

        val imagePath = "test_image_path"

        composeTestRule.setContent {
            PreviewContent(
                imagePath = imagePath,
                onShareClicked = { shareClicked = true },
                onDeleteClicked = { deleteClicked = true },
                onBackPressed = {}
            )
        }

        // Perform click on share button
        composeTestRule.onNodeWithContentDescription("Share").performClick()
        assert(shareClicked)

        // Perform click on delete button
        composeTestRule.onNodeWithContentDescription("Delete").performClick()
        assert(deleteClicked)
    }
}
