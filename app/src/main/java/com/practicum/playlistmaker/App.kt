package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator

const val PRACTICUM_PREFERENCES = "playlist_maker_preferences"

class App : Application() {

    val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(PRACTICUM_PREFERENCES, MODE_PRIVATE)
    }

    override fun onCreate() {

        super.onCreate()

        val settingsInteractor = Creator.provideSettingsInteractorImpl(sharedPreferences)
        AppCompatDelegate.setDefaultNightMode(
            if (settingsInteractor.getThemeSettings().isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}