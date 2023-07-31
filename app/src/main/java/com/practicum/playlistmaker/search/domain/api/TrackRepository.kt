package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.utils.Resource

interface TrackRepository {
    fun search(expression: String): Resource<ArrayList<Track>>
}