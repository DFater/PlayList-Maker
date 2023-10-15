package com.practicum.playlistmaker.media.ui.models

import com.practicum.playlistmaker.media.domain.models.Playlist

sealed interface NewPlaylistScreenState {

    object Empty : NewPlaylistScreenState

    data class NotEmpty(val content: Playlist) : NewPlaylistScreenState

    data class Filled(val content: Playlist) : NewPlaylistScreenState
}