@file:OptIn(ExperimentalMaterial3Api::class)
@file:SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

package com.photoweather.weather.ui.edit

import android.annotation.SuppressLint
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.photoweather.R
import com.photoweather.common.ui.theme.PhotoWeatherTheme
import com.photoweather.utils.formatShortTimeDate
import com.photoweather.utils.getErrorMessage
import com.photoweather.weather.data.LocationInfo
import com.photoweather.weather.data.WeatherInfo

@Composable
fun EditPreviewContent(
    imagePath: String,
    state: EditPreviewViewModel.ViewState,
    location: LocationInfo?,
    weatherInfo: WeatherInfo?,
    errorMsg: Exception?,
    onRetryClicked: () -> Unit,
    onConfirmClicked: (View) -> Unit,
    onBackPressed: () -> Unit
) {
    PhotoWeatherTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    val view = LocalView.current
                    Header(
                        isConfirmEnabled = location != null && weatherInfo != null,
                        isLoading = state is EditPreviewViewModel.ViewState.Saving,
                        onConfirm = { onConfirmClicked(view) },
                        onBackPressed = onBackPressed,
                    )
                },
                content = {
                    ImagePreview(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("ImagePreview")
                            .padding(top = it.calculateTopPadding()),
                        imagePath = imagePath,
                    )
                    if (location != null && weatherInfo != null) {
                        Spacer(modifier = Modifier.height(250.dp))
                        WeatherInfo(
                            modifier = Modifier
                                .padding(top = it.calculateTopPadding()).testTag("WeatherInfo")
                                .background(color = Color.Black.copy(alpha = .3f), shape = RoundedCornerShape(4.dp)),
                            weatherInfo = weatherInfo,
                            address = location.address
                        )
                    } else if (errorMsg != null) {
                        ErrorPreview(errorMsg, onRetryClicked)
                    } else {
                        ProgressBar()
                    }
                }
            )
        }
    }
}

@Composable
private fun Header(isConfirmEnabled: Boolean, isLoading: Boolean, onConfirm: () -> Unit, onBackPressed: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.edit_image)) },
        navigationIcon = {
            IconButton(onClick = { onBackPressed() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.action_back)
                )
            }
        },
        actions = {
            if (isLoading) {
                ProgressBar()
            } else {
                IconButton(onClick = { onConfirm() }, enabled = isConfirmEnabled) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = stringResource(id = R.string.action_confirm)
                    )
                }
            }
        }
    )
}

@Composable
private fun ImagePreview(modifier: Modifier = Modifier, imagePath: String) {
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
private fun WeatherInfo(modifier: Modifier = Modifier, weatherInfo: WeatherInfo, address: String) {
    Column(modifier = modifier.fillMaxWidth().padding(12.dp)) {
        Row {
            Text(
                text = weatherInfo.country,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = address,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        Row {
            Image(
                painterResource(R.drawable.ic_thermometer),
                contentDescription = "",
                modifier = Modifier.size(56.dp)
            )
            Text(text = weatherInfo.formatTemperatureCelsius(), fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Text(text = weatherInfo.description, color = Color.White)
                Text(text = formatShortTimeDate(weatherInfo.date), color = Color.White)
            }
        }
    }
}

@Composable
private fun ProgressBar() {
    Column(
        modifier = Modifier.fillMaxSize().testTag("ProgressBar"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
private fun ErrorPreview(message: Exception, onRetryClicked: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().testTag("ErrorPreview"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message.getErrorMessage(LocalContext.current), textAlign = TextAlign.Center)
        Button(onClick = onRetryClicked) {
            Text(text = stringResource(id = R.string.retry))
        }
    }
}