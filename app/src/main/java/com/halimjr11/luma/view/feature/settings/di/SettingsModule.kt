package com.halimjr11.luma.view.feature.settings.di

import com.halimjr11.luma.ui.helper.LanguageManager
import com.halimjr11.luma.ui.helper.ThemeManager
import com.halimjr11.luma.view.feature.settings.SettingsActivity
import com.halimjr11.luma.view.feature.settings.viewmodel.SettingsViewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun loadSettingsModule() {
    val helperModule = module {
        scope<SettingsActivity> { scoped { LanguageManager() } }
        scope<SettingsActivity> { scoped { ThemeManager(get()) } }
    }
    val viewModelModule = module {
        scope<SettingsActivity> { scoped { SettingsViewModel(get(), get(), get(), get()) } }
    }
    loadKoinModules(listOf(viewModelModule, helperModule))
}