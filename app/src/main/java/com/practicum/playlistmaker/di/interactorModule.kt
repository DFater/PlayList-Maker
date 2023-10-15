package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.domain.api.FavouriteTrackInteractor
import com.practicum.playlistmaker.media.domain.api.LocalStorageInteractor
import com.practicum.playlistmaker.media.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.media.domain.impl.FavouriteTrackInteractorImpl
import com.practicum.playlistmaker.media.domain.impl.LocalStorageInteractorImpl
import com.practicum.playlistmaker.media.domain.impl.PlaylistInteractorImpl
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TrackInteractor
import com.practicum.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.setting.domain.api.SettingsInteractor
import com.practicum.playlistmaker.setting.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val interactorModule = module {
    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    single<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }

    single<TrackInteractor> {
        TracksInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<FavouriteTrackInteractor> {
        FavouriteTrackInteractorImpl(get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

    single<LocalStorageInteractor> {
        LocalStorageInteractorImpl(get(named("imageDirectory")))
    }
}