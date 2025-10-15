package com.halimjr11.luma.ui.helper

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

class LanguageManager() {
    fun getCurrentLanguage(): String {
        val locale = Locale.getDefault()
        return locale.language
    }

    fun updateResources(language: String) {
        val appLocale: LocaleListCompat =
            LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(appLocale)

    }
}