@file:OptIn(ExperimentalMaterial3Api::class)
@file:SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

package com.photoweather.weather.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.photoweather.R
import com.photoweather.common.ui.theme.PhotoWeatherTheme
import com.photoweather.weather.data.Gallery
import timber.log.Timber

@Composable
fun MainContent(
    galleryItems: List<Gallery>,
    state: MainViewModel.ViewState,
    onPhotoCaptureClicked: () -> Unit,
    onItemClicked: (Gallery) -> Unit,
    onPermissionDenied: () -> Unit,
    onDialogDismissed: () -> Unit
) {
    val scrollState = rememberLazyGridState()
    PhotoWeatherTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    Header()
                },
                floatingActionButton = {
                    Footer(onPhotoCaptureClicked = onPhotoCaptureClicked)
                },
                content = {
                    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 132.dp),
                        contentPadding = PaddingValues(
                            start = 8.dp,
                            top = it.calculateTopPadding(),
                            end = 8.dp,
                            bottom = 8.dp
                        ),
                        state = scrollState,
                        content = {
                            items(galleryItems) { item ->
                                ViewHolder(
                                    modifier = Modifier.testTag("ViewHolder"),
                                    item = item,
                                    onItemClicked = onItemClicked
                                )
                            }
                        })
                    if (state is MainViewModel.ViewState.PermissionDenied) {
                        PermissionDeniedAlertDialog(
                            title = state.title,
                            description = state.message,
                            onPermissionDenied = onPermissionDenied,
                            onDismiss = onDialogDismissed,
                        )
                    }
                },
            )
        }
    }
}

@Composable
private fun Header() {
    TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
}

@Composable
private fun PermissionDeniedAlertDialog(
    @StringRes title: Int,
    @StringRes description: Int,
    onPermissionDenied: () -> Unit,
    onDismiss: () -> Unit,
) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == ComponentActivity.RESULT_OK && it.data != null) {
            onDismiss()
        } else {
            Timber.d("Consent denied. User denied permission.")
            onPermissionDenied()
        }
    }
    val packageName = LocalContext.current.packageName
    val data = Uri.fromParts("package", packageName, null)
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(data)
    AlertDialog(
        modifier = Modifier.testTag("Permission"),
        title = { Text(text = stringResource(title)) },
        text = { Text(text = stringResource(description)) },
        confirmButton = {
            Button(onClick = {
                launcher.launch(intent)
                onDismiss()
            }) {
                Text(stringResource(id = R.string.settings))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text(stringResource(id = R.string.cancel)) }
        },
        onDismissRequest = onDismiss,
    )
}

@Composable
private fun ViewHolder(
    modifier: Modifier = Modifier,
    item: Gallery,
    onItemClicked: (Gallery) -> Unit,
) {
    val painter = ImageRequest.Builder(LocalContext.current)
        .data(item.imagePath)
        .crossfade(true)
        .build()
    SubcomposeAsyncImage(
        model = painter,
        loading = {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp)
            )
        },
        contentDescription = stringResource(R.string.description),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .aspectRatio(9f / 14)
            .padding(4.dp)
            .clickable { onItemClicked(item) },
    )
}

@Composable
private fun Footer(
    modifier: Modifier = Modifier,
    onPhotoCaptureClicked: () -> Unit,
) {
    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = modifier
            .clip(CircleShape)
            .testTag("FAB"),
        onClick = onPhotoCaptureClicked,
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = stringResource(id = R.string.camera),
            tint = Color.White,
        )
    }
}