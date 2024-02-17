package com.practicum.playlistmaker.player.ui.models

import com.practicum.playlistmaker.media.domain.models.Playlist

sealed interface PlayerScreenMode {
    object Player : PlayerScreenMode

    object NewPlaylist : PlayerScreenMode

    data class BottomSheet(val playlists: List<Playlist>) : PlayerScreenMode
}