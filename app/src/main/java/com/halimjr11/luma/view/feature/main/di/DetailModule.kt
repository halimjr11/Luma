package com.halimjr11.luma.view.feature.main.di

import com.halimjr11.luma.view.feature.main.fragments.DetailStoryFragment
import com.halimjr11.luma.view.feature.main.viewmodel.DetailViewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun loadDetailModule() {
    val viewModelModule = module {
        scope<DetailStoryFragment> { scoped { DetailViewModel(get(), get(), get()) } }
    }
    loadKoinModules(viewModelModule)
}