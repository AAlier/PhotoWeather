package com.photoweather.weather.ui.edit

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.photoweather.utils.args
import com.photoweather.utils.createImageFile
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPreviewActivity : ComponentActivity() {

    companion object {
        private const val EXTRA_IMAGE = "image"

        fun start(context: Context, uri: Uri) {
            val intent = Intent(context, EditPreviewActivity::class.java)
            intent.putExtra(EXTRA_IMAGE, uri.toString())
            context.startActivity(intent)
        }
    }

    private val viewModel: EditPreviewViewModel by viewModel()
    private val imagePath: String by args(EXTRA_IMAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val location by viewModel.locationInfo.collectAsStateWithLifecycle()
            val weatherInfo by viewModel.weatherInfo.collectAsStateWithLifecycle()
            val error by viewModel.error.collectAsStateWithLifecycle()
            val state by viewModel.state.collectAsStateWithLifecycle()
            EditPreviewContent(
                imagePath = imagePath,
                state = state,
                location = location,
                weatherInfo = weatherInfo,
                errorMsg = error,
                onRetryClicked = {
                    viewModel.onRetry(this)
                },
                onConfirmClicked = { view ->
                    viewModel.save(this.createImageFile(), view, window)
                },
                onBackPressed = { finish() },
            )
        }
        viewModel.startLocationUpdate(this)
        registerStateListener()
    }

    private fun registerStateListener() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    EditPreviewViewModel.ViewState.Saved -> finish()
                    else -> return@collect
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopLocationUpdate()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopLocationUpdate()
    }
}