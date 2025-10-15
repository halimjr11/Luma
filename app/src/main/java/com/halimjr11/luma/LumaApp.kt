package com.halimjr11.luma

import android.app.Application
import com.halimjr11.luma.di.AppModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class LumaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@LumaApp)
            modules(AppModules.getAppModules())
        }
    }
}
