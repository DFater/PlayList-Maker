package com.practicum.playlistmaker.media.data.impl

import com.practicum.playlistmaker.db.AppDatabase
import com.practicum.playlistmaker.media.data.PlaylistDbConvertor
import com.practicum.playlistmaker.media.data.PlaylistTrackDbConvertor
import com.practicum.playlistmaker.media.domain.api.PlaylistRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val playlistTrackDbConvertor: PlaylistTrackDbConvertor
) : PlaylistRepository {

    override suspend fun addPlaylist(playlist: Playlist): Long {
        val playlistEntity = playlistDbConvertor.map(playlist)
        return try {
            val result = appDatabase.playlistDao().insertPlaylist(playlistEntity)
            result
        } catch (e: Exception) {
            0
        }
    }

    override suspend fun getPlaylistById(id: Long): Playlist {
        return playlistDbConvertor.map(appDatabase.playlistDao().getPlaylistById(id))
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getPlaylists()
            .map { it.map { playlistEntity -> playlistDbConvertor.map(playlistEntity) } }
    }

    override suspend fun addTrackToPlaylist(track: Track, playlistId: Long) {
        val playlist = getPlaylistById(playlistId)
        appDatabase.playlistTrackDao().insertPlaylistTrack(playlistTrackDbConvertor.map(track))
        playlist.trackList.add(track.trackId!!)
        playlist.trackCount += 1
        appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(playlist))
    }
}