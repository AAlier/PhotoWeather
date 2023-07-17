package com.photoweather

import android.app.Application
import com.photoweather.common.database.StorageModule
import com.photoweather.common.network.NetworkModule
import com.photoweather.weather.WeatherModule
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class PhotoWeatherApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
        setupTimber()
    }

    private fun setupKoin() {
        GlobalContext.stopKoin()
        startKoin {
            androidContext(this@PhotoWeatherApplication)
            printLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)
            allowOverride(true)
            modules(
                listOf(
                    StorageModule.create(),
                    NetworkModule.create(),
                    WeatherModule.create(),
                )
            )
        }
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}