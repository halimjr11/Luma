package com.halimjr11.luma.view.feature.create.di

import com.halimjr11.luma.view.feature.create.fragments.CreateStoryFragment
import com.halimjr11.luma.view.feature.create.viewmodel.CreateViewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun loadCreateModule() {
    val viewModelModule = module {
        scope<CreateStoryFragment> { scoped { CreateViewModel(get(), get(), get()) } }
    }
    loadKoinModules(viewModelModule)
}
