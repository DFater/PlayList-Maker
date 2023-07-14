package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Tracks

interface TracksRepository {
    fun search(expression: String, onEmpty: () -> Unit, onFailure: () -> Unit): ArrayList<Tracks>
}