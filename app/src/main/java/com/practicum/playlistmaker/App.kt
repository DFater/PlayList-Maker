package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val PRACTICUM_PREFERENCES = "playlist_maker_preferences"


class App : Application() {

    var darkTheme = false
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(PRACTICUM_PREFERENCES, MODE_PRIVATE)
    }

    override fun onCreate() {

        super.onCreate()
        darkTheme = sharedPreferences.getBoolean(THEME_KEY, darkTheme)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )
    }

    fun saveAppPreferences(key: String, value: Boolean) {
        sharedPreferences.edit()
            .putBoolean(key, value)
            .apply()
    }

    companion object {
        const val THEME_KEY = "theme_key"
    }
}