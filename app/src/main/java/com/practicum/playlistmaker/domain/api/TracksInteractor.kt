package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun search(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: ArrayList<Track>)
        fun onEmpty()
        fun onFailure()
    }
}