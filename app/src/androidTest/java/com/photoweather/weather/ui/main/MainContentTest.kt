package com.photoweather.weather.ui.main

import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.photoweather.R
import com.photoweather.weather.data.Gallery
import org.junit.Rule
import org.junit.Test

class MainContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testMainContent() {
        val galleryItems = listOf(
            Gallery(1, "image1.jpg"),
            Gallery(2, "image2.jpg"),
            Gallery(3, "image3.jpg")
        )
        val state = MainViewModel.ViewState.None

        composeTestRule.setContent {
            MainContent(
                galleryItems = galleryItems,
                state = state,
                onPhotoCaptureClicked = {},
                onItemClicked = {},
                onPermissionDenied = {},
                onDialogDismissed = {}
            )
        }

        composeTestRule.onAllNodesWithTag("ViewHolder").assertAny(hasTestTag("ViewHolder"))
        composeTestRule.onNodeWithTag("FAB").assertExists()
        composeTestRule.onNodeWithContentDescription("Camera").assertExists()
        composeTestRule.onNodeWithContentDescription("Camera").performClick()
    }

    @Test
    fun permissionPreview() {
        val state = MainViewModel.ViewState.PermissionDenied(
            R.string.storage_permission_alert_title,
            R.string.storage_permission_alert_message
        )

        composeTestRule.setContent {
            MainContent(
                galleryItems = listOf(),
                state = state,
                onPhotoCaptureClicked = {},
                onItemClicked = {},
                onPermissionDenied = {},
                onDialogDismissed = {}
            )
        }

        composeTestRule.onNodeWithTag("Permission").performClick()
        composeTestRule.onNodeWithTag("Permission").assertExists()
    }

    @Test
    fun permissionPreviewNotShown() {
        val state = MainViewModel.ViewState.None

        composeTestRule.setContent {
            MainContent(
                galleryItems = listOf(),
                state = state,
                onPhotoCaptureClicked = {},
                onItemClicked = {},
                onPermissionDenied = {},
                onDialogDismissed = {}
            )
        }

        composeTestRule.onNodeWithTag("Permission").assertDoesNotExist()
    }
}