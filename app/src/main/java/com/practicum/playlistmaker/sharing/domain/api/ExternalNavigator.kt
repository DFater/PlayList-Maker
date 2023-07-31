package com.practicum.playlistmaker.sharing.domain.api

import com.practicum.playlistmaker.sharing.domain.models.EmailData
import com.practicum.playlistmaker.sharing.domain.models.ShareData

interface ExternalNavigator {
    fun shareApp(link: ShareData)
    fun openTermsOfUse(link: ShareData)
    fun openSupport(email: EmailData)
}