package com.halimjr11.luma.view.feature.main.di

import com.halimjr11.luma.view.feature.main.fragments.MainStoryFragment
import com.halimjr11.luma.view.feature.main.viewmodel.MainViewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun loadMainModule() {
    val viewModelModule = module {
        scope<MainStoryFragment> { scoped { MainViewModel(get()) } }
    }
    loadKoinModules(viewModelModule)
}