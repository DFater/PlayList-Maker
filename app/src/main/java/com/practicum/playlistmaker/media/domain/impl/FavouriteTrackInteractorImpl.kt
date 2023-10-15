package com.practicum.playlistmaker.media.domain.impl

import com.practicum.playlistmaker.media.domain.api.FavouriteTrackInteractor
import com.practicum.playlistmaker.media.domain.api.FavouriteTrackRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavouriteTrackInteractorImpl(private val favouriteTrackRepository: FavouriteTrackRepository) :
    FavouriteTrackInteractor {
    override suspend fun getFavouriteTracks(): Flow<List<Track>> {
        return favouriteTrackRepository.getFavouriteTracks()
    }

    override suspend fun saveFavouriteTrack(track: Track) {
        favouriteTrackRepository.saveFavouriteTrack(track)
    }

    override suspend fun deleteFavouriteTrack(trackId: Long) {
        favouriteTrackRepository.deleteFavouriteTrack(trackId)
    }

    override suspend fun getFavouriteState(trackId: Long): Boolean {
        return favouriteTrackRepository.getFavouriteState(trackId)
    }
}