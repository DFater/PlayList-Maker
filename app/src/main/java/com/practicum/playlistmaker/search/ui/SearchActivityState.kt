package com.practicum.playlistmaker.search.ui

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface SearchActivityState {

    object Loading : SearchActivityState

    object Empty : SearchActivityState

    object Error : SearchActivityState

    data class List(
        val tracks: ArrayList<Track>
    ) : SearchActivityState

    data class History(
        val tracks: ArrayList<Track>
    ) : SearchActivityState
}