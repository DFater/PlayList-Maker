package com.practicum.playlistmaker

import com.practicum.playlistmaker.data.impl.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.data.impl.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    fun providePlayerInteractorImpl(): PlayerInteractor {
        return PlayerInteractorImpl(MediaPlayerRepositoryImpl())
    }

    fun provideTrackInteractor(): TracksInteractor {
        return TracksInteractorImpl(
            TracksRepositoryImpl(RetrofitNetworkClient())
        )
    }
}