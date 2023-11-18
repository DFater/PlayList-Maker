package com.practicum.playlistmaker.media.ui.models

import com.practicum.playlistmaker.media.domain.models.Playlist

sealed interface NewPlaylistScreenResult {

    object Canceled : NewPlaylistScreenResult

    data class Created(val content: Playlist) : NewPlaylistScreenResult

}