package com.halimjr11.luma.ui.helper

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class ThemeManager(private val context: Context) {

    fun isSystemDarkMode(): Boolean {
        val currentNightMode = context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    fun applyTheme(isDarkMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}