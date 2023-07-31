package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.models.EmailData
import com.practicum.playlistmaker.sharing.domain.models.ShareData

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareApp(ShareData.SHARE_URL)
    }

    override fun openTermsOfUse() {
        externalNavigator.openTermsOfUse(ShareData.TERMS_OF_USE)
    }

    override fun openSupport() {
        externalNavigator.openSupport(EmailData.SUPPORT_EMAIL)
    }
}