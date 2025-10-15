package com.halimjr11.luma.view.feature.auth.di

import com.halimjr11.luma.view.feature.auth.fragments.RegisterFragment
import com.halimjr11.luma.view.feature.auth.viewmodel.RegisterViewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun loadRegisterModule() {
    val viewModelModule = module {
        scope<RegisterFragment> { scoped { RegisterViewModel(get(), get()) } }
    }
    loadKoinModules(viewModelModule)
}