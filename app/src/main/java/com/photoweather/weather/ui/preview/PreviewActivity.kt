package com.photoweather.weather.ui.preview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.photoweather.utils.args
import com.photoweather.utils.share
import com.photoweather.weather.data.Gallery
import org.koin.androidx.viewmodel.ext.android.viewModel

class PreviewActivity : ComponentActivity() {
    companion object {
        private const val EXTRA_DATA = "data"

        fun start(context: Context, item: Gallery) {
            val intent = Intent(context, PreviewActivity::class.java)
            intent.putExtra(EXTRA_DATA, item)
            context.startActivity(intent)
        }
    }

    private val viewModel: PreviewViewModel by viewModel()
    private val gallery: Gallery by args(EXTRA_DATA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PreviewContent(
                imagePath = gallery.imagePath,
                onShareClicked = {
                    share(file = gallery.toFile())
                },
                onDeleteClicked = {
                    viewModel.onDelete(gallery.id)
                    finish()
                },
                onBackPressed = { finish() },
            )
        }
    }
}