package com.photoweather.weather

import com.photoweather.common.database.PhotoWeatherDatabase
import com.photoweather.utils.PixelCopyViewCapture
import com.photoweather.utils.ViewCapture
import com.photoweather.weather.api.WeatherApi
import com.photoweather.weather.api.WeatherRemoteRepository
import com.photoweather.weather.api.WeatherRemoteRepositoryImpl
import com.photoweather.weather.database.GalleryDao
import com.photoweather.weather.database.GalleryLocalRepository
import com.photoweather.weather.database.GalleryLocalRepositoryImpl
import com.photoweather.weather.interactor.WeatherInteractor
import com.photoweather.weather.ui.edit.EditPreviewViewModel
import com.photoweather.weather.ui.main.MainViewModel
import com.photoweather.weather.ui.preview.PreviewViewModel
import kg.dcb.mobilebanking.common.di.InjectionModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

object WeatherModule : InjectionModule {
    override fun create(): Module {
        return module {
            single { get<Retrofit>().create(WeatherApi::class.java) } bind WeatherApi::class
            single { get<PhotoWeatherDatabase>().galleryDao() } bind GalleryDao::class
            singleOf(::GalleryLocalRepositoryImpl) bind GalleryLocalRepository::class
            singleOf(::WeatherRemoteRepositoryImpl) bind WeatherRemoteRepository::class
            singleOf(::WeatherInteractor)
            singleOf(::PixelCopyViewCapture) bind ViewCapture::class

            viewModelOf(::MainViewModel)
            viewModelOf(::PreviewViewModel)
            viewModelOf(::EditPreviewViewModel)
        }
    }
}