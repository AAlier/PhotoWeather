package com.photoweather.weather.ui.main

import android.Manifest
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.photoweather.utils.createImageFile
import com.photoweather.utils.isPermissionGranted
import com.photoweather.weather.ui.edit.EditPreviewActivity
import com.photoweather.weather.ui.preview.PreviewActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = createNewFile()

        setContent {
            val images by viewModel.galleryFlow.collectAsStateWithLifecycle(initialValue = emptyList())
            val state by viewModel.state.collectAsStateWithLifecycle(initialValue = MainViewModel.ViewState.None)
            val (permissionsLauncher, cameraLauncher) = launchPermissions(uri)
            MainContent(
                galleryItems = images,
                state = state,
                onPermissionDenied = {
                    finish()
                },
                onPhotoCaptureClicked = {
                    val permissions = permissions()
                    if (permissions.all { this.isPermissionGranted(it) }) {
                        cameraLauncher.launch(uri)
                    } else {
                        permissionsLauncher.launch(permissions)
                    }
                },
                onItemClicked = {
                    PreviewActivity.start(this, it)
                },
                onDialogDismissed = {
                    viewModel.resetState()
                }
            )
        }
    }

    @Composable
    private fun launchPermissions(uri: Uri): Pair<ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>, ManagedActivityResultLauncher<Uri, Boolean>> {
        val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { hasCaptured ->
            if (hasCaptured) {
                EditPreviewActivity.start(this, uri)
            }
        }
        val launcherMultiplePermissions = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionsMap ->
            val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
            if (areGranted) {
                cameraLauncher.launch(uri)
            } else {
                viewModel.onPermissionsDenied(permissionsMap.filter { !it.value }.keys.first())
            }
        }
        return launcherMultiplePermissions to cameraLauncher
    }

    private fun createNewFile(): Uri {
        val file = createImageFile()
        return FileProvider.getUriForFile(this, "$packageName.provider", file)
    }

    private fun permissions() = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
}