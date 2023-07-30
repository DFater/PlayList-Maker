package com.practicum.playlistmaker.setting.domain.api

import com.practicum.playlistmaker.setting.domain.models.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}