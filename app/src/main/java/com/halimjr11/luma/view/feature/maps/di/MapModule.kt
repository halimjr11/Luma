package com.halimjr11.luma.view.feature.maps.di

import com.halimjr11.luma.view.feature.maps.MapsActivity
import com.halimjr11.luma.view.feature.maps.viewmodel.MapViewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun loadMapModule() {
    val viewModel = module {
        scope<MapsActivity> { scoped { MapViewModel(get(), get()) } }
    }
    loadKoinModules(viewModel)
}