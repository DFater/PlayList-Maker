package com.practicum.playlistmaker.media.ui

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface FavouriteTrackScreenState {

    object Loading : FavouriteTrackScreenState

    object Empty : FavouriteTrackScreenState

    data class Content(val tracks: List<Track>) : FavouriteTrackScreenState

}