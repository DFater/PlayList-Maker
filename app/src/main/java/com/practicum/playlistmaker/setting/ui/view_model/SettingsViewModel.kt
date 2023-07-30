package com.practicum.playlistmaker.setting.ui.view_model

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.setting.domain.api.SettingsInteractor
import com.practicum.playlistmaker.setting.domain.models.ThemeSettings
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
    application: Application
) : AndroidViewModel(application) {

    private val darkThemeLiveData =
        MutableLiveData(settingsInteractor.getThemeSettings().isDarkTheme)

    fun observeDarkTheme(): LiveData<Boolean> = darkThemeLiveData


    fun clickOnShareApp() {
        sharingInteractor.shareApp()
    }

    fun clickOnServiceButton() {
        sharingInteractor.openSupport()
    }

    fun clickOnTermsOfUseButton() {
        sharingInteractor.openTermsOfUse()
    }

    fun switchTheme(isDarkTheme: Boolean) {
        if (darkThemeLiveData.value != isDarkTheme) {
            settingsInteractor.updateThemeSetting(ThemeSettings(isDarkTheme))
            darkThemeLiveData.value = isDarkTheme
            setAppDarkTheme(isDarkTheme)
        }
    }

    private fun setAppDarkTheme(isDarkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        fun getViewModelFactory(sharedPreferences: SharedPreferences): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val application =
                        this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                    SettingsViewModel(
                        Creator.provideSharingInteractorImpl(application),
                        Creator.provideSettingsInteractorImpl(sharedPreferences),
                        application
                    )
                }
            }
    }
}