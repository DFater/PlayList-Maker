package com.practicum.playlistmaker.media.ui.models

import com.practicum.playlistmaker.media.domain.models.Playlist

sealed interface PlaylistInfoScreenState {

    object Loading : PlaylistInfoScreenState

    data class Error(val message: String) : PlaylistInfoScreenState

    data class Content(val data: Playlist) : PlaylistInfoScreenState
}