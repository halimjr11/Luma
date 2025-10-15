package com.halimjr11.luma.view.feature.auth.di

import com.halimjr11.luma.view.feature.auth.fragments.LoginFragment
import com.halimjr11.luma.view.feature.auth.viewmodel.LoginViewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun loadLoginModule() {
    val viewModelModule = module {
        scope<LoginFragment> { scoped { LoginViewModel(get(), get()) } }
    }
    loadKoinModules(viewModelModule)
}