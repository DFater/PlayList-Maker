package com.practicum.playlistmaker.media.data.impl

import com.practicum.playlistmaker.db.AppDatabase
import com.practicum.playlistmaker.media.data.PlaylistDbConvertor
import com.practicum.playlistmaker.media.data.PlaylistTrackDbConvertor
import com.practicum.playlistmaker.media.domain.api.PlaylistRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.Exception


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

    override suspend fun deletePlaylist(playlist: Playlist) {
        val tracksIdList = playlist.trackList.toList()
        val entity = playlistDbConvertor.map(playlist)
        appDatabase.playlistDao().deletePlaylist(entity)
        tracksIdList.forEach { trackId ->
            if (isUnusedPlaylistTrack(trackId)) {
                val track = appDatabase.playlistTrackDao().getPlaylistTrackById(trackId)
                appDatabase.playlistTrackDao().deletePlaylistTrack(track)
            }
        }
    }

    override suspend fun getPlaylistById(id: Long): Playlist? {
        val playlistEntity = appDatabase.playlistDao().getPlaylistById(id)
        return playlistEntity?.let {
            playlistDbConvertor.map(it)
        }
    }

    override suspend fun getPlaylists(): List<Playlist> {
        return appDatabase.playlistDao().getPlaylists()
            .map { playlistEntity -> playlistDbConvertor.map(playlistEntity) }
    }

    override suspend fun getFlowPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getFlowPlaylists()
            .map { it.map { playlistEntity -> playlistDbConvertor.map(playlistEntity) } }
    }

    override suspend fun addTrackToPlaylist(track: Track, playlistId: Long) {
        val playlist = getPlaylistById(playlistId)
        if (playlist != null) {
            appDatabase.playlistTrackDao().insertPlaylistTrack(playlistTrackDbConvertor.map(track))
            playlist.trackList.add(track.trackId!!)
            playlist.trackCount += 1
            appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(playlist))
        }

    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlistId: Long) {
        val playlist = getPlaylistById(playlistId)
        if (playlist != null) {
            playlist.trackList.remove(track.trackId)
            playlist.trackCount -= 1
            appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(playlist))
            val trackId = track.trackId ?: 0
            if (trackId > 0 && isUnusedPlaylistTrack(trackId)) {
                appDatabase.playlistTrackDao()
                    .deletePlaylistTrack(playlistTrackDbConvertor.map(track))
            }
        }

    }

    override suspend fun getFlowPlaylistById(id: Long): Flow<Playlist?> {
        val flowPlaylistEntity = appDatabase.playlistDao().getFlowPlaylistById(id)
        return (flowPlaylistEntity.map { playlistEntity ->
            if (playlistEntity != null) playlistDbConvertor.map(
                playlistEntity
            ) else null
        })
    }

    override suspend fun getPlaylistTracksByTrackIdList(playlistIds: List<Long>): List<Track> {
        val playlist = appDatabase.playlistTrackDao().getPlaylistTracks()
        return playlist
            .filter { it.trackId in playlistIds }
            .map { playlistTrackEntity -> playlistTrackDbConvertor.map(playlistTrackEntity) }
    }

    private suspend fun isUnusedPlaylistTrack(trackId: Long): Boolean {
        val playlists =
            getPlaylists().filter { playlist -> playlist.trackList.contains(trackId) }
        return playlists.isEmpty()
    }
}