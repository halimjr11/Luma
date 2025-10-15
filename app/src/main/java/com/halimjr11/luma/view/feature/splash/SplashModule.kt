package com.halimjr11.luma.view.feature.splash

import com.halimjr11.luma.view.feature.splash.viewmodel.SplashViewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun loadSplashModule() {
    val viewModelModule = module {
        scope<SplashActivity> { scoped { SplashViewModel(get(), get()) } }
    }
    loadKoinModules(viewModelModule)
}