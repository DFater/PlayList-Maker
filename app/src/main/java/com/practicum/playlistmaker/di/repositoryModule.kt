package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.data.PlaylistDbConvertor
import com.practicum.playlistmaker.media.data.PlaylistTrackDbConvertor
import com.practicum.playlistmaker.media.data.TrackDbConvertor
import com.practicum.playlistmaker.media.data.impl.FavouriteTrackRepositoryImpl
import com.practicum.playlistmaker.media.data.impl.PlaylistRepositoryImpl
import com.practicum.playlistmaker.media.domain.api.FavouriteTrackRepository
import com.practicum.playlistmaker.media.domain.api.PlaylistRepository
import com.practicum.playlistmaker.player.data.impl.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.search.data.impl.HistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.setting.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.setting.domain.api.SettingsRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory<PlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(get(), get())
    }

    single<TrackRepository> {
        TracksRepositoryImpl(get(), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<FavouriteTrackRepository> {
        FavouriteTrackRepositoryImpl(get(), get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get())
    }

    factory {
        TrackDbConvertor()
    }

    factory {
        PlaylistDbConvertor(get())
    }

    factory {
        PlaylistTrackDbConvertor()
    }
}