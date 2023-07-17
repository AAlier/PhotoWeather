@file:OptIn(ExperimentalMaterial3Api::class)
@file:SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

package com.photoweather.weather.ui.preview

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.photoweather.R
import com.photoweather.common.ui.theme.PhotoWeatherTheme

@Composable
fun PreviewContent(
    imagePath: String,
    onShareClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onBackPressed: () -> Unit
) {
    PhotoWeatherTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    Header(onBackPressed)
                },
                content = {
                    ImagePreview(
                        modifier = Modifier.padding(top = it.calculateTopPadding()).testTag("Preview"),
                        imagePath = imagePath,
                    )
                    Footer(onShareClicked = onShareClicked, onDeleteClicked = onDeleteClicked)
                }
            )
        }
    }
}

@Composable
private fun Header(onBackPressed: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.preview_image)) },
        navigationIcon = {
            IconButton(onClick = { onBackPressed() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.action_back)
                )
            }
        }
    )
}

@Composable
private fun ImagePreview(
    modifier: Modifier = Modifier,
    imagePath: String,
) {
    val painter = ImageRequest.Builder(LocalContext.current)
        .data(imagePath)
        .crossfade(true)
        .build()
    SubcomposeAsyncImage(
        modifier = modifier,
        model = painter,
        loading = {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp)
            )
        },
        contentDescription = stringResource(R.string.description),
    )
}

@Composable
private fun Footer(
    modifier: Modifier = Modifier,
    onShareClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
) {
    Column(
        Modifier.fillMaxHeight().testTag("Footer"),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = modifier)
        Row(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f, false)
                .background(Color.Black.copy(alpha = .3f))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { onShareClicked() }) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = stringResource(id = R.string.action_share)
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            IconButton(onClick = { onDeleteClicked() }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(id = R.string.action_delete)
                )
            }
        }
    }
}