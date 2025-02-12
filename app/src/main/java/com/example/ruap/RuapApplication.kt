package com.example.ruap

import android.app.Application
import com.example.ruap.data.di.remoteDataModule
import com.example.ruap.ui.result.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RuapApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RuapApplication)
            modules(remoteDataModule, viewModelModule)
        }
    }
}