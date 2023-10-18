package com.practicum.playlistmaker.media.domain.api

import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun addPlaylist(playlist: Playlist): Long

    suspend fun getPlaylistById(id: Long): Playlist

    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(track: Track, playlistId: Long)
}