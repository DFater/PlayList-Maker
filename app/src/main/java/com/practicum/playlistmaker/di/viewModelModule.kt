package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.ui.view_model.FavouritesViewModel
import com.practicum.playlistmaker.media.ui.view_model.NewPlaylistViewModel
import com.practicum.playlistmaker.media.ui.view_model.NewPlaylistEditViewModel
import com.practicum.playlistmaker.media.ui.view_model.PlaylistInfoViewModel
import com.practicum.playlistmaker.media.ui.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.setting.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel { (track: Track) ->
        PlayerViewModel(track, get(), get(), get(), get())
    }

    viewModel {
        FavouritesViewModel(get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

    viewModel {
        NewPlaylistViewModel(get(), get())
    }

    viewModel { (playlistId: Long) ->
        PlaylistInfoViewModel(playlistId, get(), get())
    }

    viewModel {(playlist: Playlist) ->
        NewPlaylistEditViewModel(playlist, get(), get())
    }
}