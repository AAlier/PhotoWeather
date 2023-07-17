package com.photoweather.common.database

import androidx.room.Room
import com.photoweather.common.database.PhotoWeatherDatabase.Companion.DATABASE_NAME
import kg.dcb.mobilebanking.common.di.InjectionModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import java.util.concurrent.Executors

object StorageModule: InjectionModule {
    override fun create(): Module = module {
        single {
            Room
                .databaseBuilder(androidContext(), PhotoWeatherDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .setQueryExecutor(Executors.newCachedThreadPool())
                .build()
        }
    }
}