package com.practicum.playlistmaker.media.data.impl

import com.practicum.playlistmaker.db.AppDatabase
import com.practicum.playlistmaker.media.data.TrackDbConvertor
import com.practicum.playlistmaker.media.data.entity.TrackEntity
import com.practicum.playlistmaker.media.domain.api.FavouriteTrackRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavouriteTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavouriteTrackRepository {

    override suspend fun getFavouriteTracks(): Flow<List<Track>> {
        return appDatabase.trackDao().getTracks().map { it.map { trackEntity -> trackDbConvertor.map(trackEntity) } }
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

}