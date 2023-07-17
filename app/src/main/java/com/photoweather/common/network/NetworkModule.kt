package com.photoweather.common.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.photoweather.BuildConfig
import com.photoweather.common.location.LocationManager
import com.photoweather.common.network.NetworkModule.DEFAULT_CONNECT_TIMEOUT_SECONDS
import com.photoweather.common.network.NetworkModule.DEFAULT_READ_TIMEOUT_SECONDS
import kg.dcb.mobilebanking.common.di.InjectionModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.singleOf
import org.koin.core.scope.Scope
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Network Configuration - responsible for:
 * 1. [DEFAULT_CONNECT_TIMEOUT_SECONDS] / [DEFAULT_READ_TIMEOUT_SECONDS]
 * 2. Interceptor:
 *          1. Server Time Interceptor -> Fetch Time with Server
 *          2. Server Error Handling
 * 3. HttpLoggingInterceptor - Logging network requests
 * */
object NetworkModule : InjectionModule {

    private const val DEFAULT_CONNECT_TIMEOUT_SECONDS = 90L
    private const val DEFAULT_READ_TIMEOUT_SECONDS = 90L

    override fun create() = module {
        singleOf(::LocationManager)
        single {
            GsonBuilder()
                .apply { if (BuildConfig.DEBUG) setPrettyPrinting() }
                .create()
        }

        single {
            Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(get()))
                .client(get())
                .build()
        }
        single { createOkHttpClient().build() }
    }

    private fun Scope.createLoggingInterceptor(): HttpLoggingInterceptor {
        val gson = get<Gson>()
        val jsonParser = JsonParser()
        val okHttpLogTag = "OkHttp"

        val okHttpLogger = object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                if (!message.startsWith('{') && !message.startsWith('[')) {
                    Timber.tag(okHttpLogTag).d(message)
                    return
                }
                try {
                    val json = jsonParser.parse(message)
                    Timber.tag(okHttpLogTag).d(gson.toJson(json))
                } catch (e: Throwable) {
                    Timber.tag(okHttpLogTag).d(message)
                }
            }
        }
        return HttpLoggingInterceptor(okHttpLogger).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private fun Scope.createOkHttpClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .writeTimeout(DEFAULT_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .hostnameVerifier { _, _ -> true }
            .apply {
                if (BuildConfig.DEBUG)
                    addInterceptor(createLoggingInterceptor())
            }
    }
}