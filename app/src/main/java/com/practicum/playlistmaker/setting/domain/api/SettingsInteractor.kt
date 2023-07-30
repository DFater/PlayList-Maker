package com.practicum.playlistmaker.setting.domain.api

import com.practicum.playlistmaker.setting.domain.models.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}