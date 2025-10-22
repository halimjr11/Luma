package com.halimjr11.luma.view.feature.favorite.di

import com.halimjr11.luma.view.feature.favorite.FavoriteActivity
import com.halimjr11.luma.view.feature.favorite.viewmodel.FavoriteViewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun loadFavoriteModule() {
    val viewModel = module {
        scope<FavoriteActivity> { scoped { FavoriteViewModel(get()) } }
    }
    loadKoinModules(viewModel)
}