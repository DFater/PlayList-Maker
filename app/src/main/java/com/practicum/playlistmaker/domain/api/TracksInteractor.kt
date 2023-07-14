package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Tracks

interface TracksInteractor {
    fun search(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: ArrayList<Tracks>)
        fun onEmpty()
        fun onFailure()
    }
}