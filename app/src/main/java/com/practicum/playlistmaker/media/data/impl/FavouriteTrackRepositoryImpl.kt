package com.practicum.playlistmaker.media.data.impl

import com.practicum.playlistmaker.db.AppDatabase
import com.practicum.playlistmaker.media.data.TrackDbConvertor
import com.practicum.playlistmaker.media.data.entity.TrackEntity
import com.practicum.playlistmaker.media.domain.api.FavouriteTrackRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavouriteTrackRepository {

    override fun getFavouriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(mapFromTrackEntity(tracks))
    }

    override suspend fun getFavouriteState(trackId: Long): Boolean {
        return appDatabase.trackDao().getTrackIds().indexOf(trackId) > -1
    }

    override suspend fun saveFavouriteTrack(track: Track) {
        val trackEntity = trackDbConvertor.map(track)
        appDatabase.trackDao().insertTrack(trackEntity)
    }

    override suspend fun deleteFavouriteTrack(trackId: Long) {
        appDatabase.trackDao().deleteTrack(trackId)
    }

    private fun mapFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
}