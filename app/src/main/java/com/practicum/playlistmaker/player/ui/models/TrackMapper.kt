package com.practicum.playlistmaker.player.ui.models

import com.practicum.playlistmaker.search.domain.models.Track

object TrackMapper {

    fun trackMap(track: Track): ParcelableTrack {
        return ParcelableTrack(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis ?: 0,
            track.artworkUrl100,
            track.albumName,
            track.releaseYear,
            track.genreName,
            track.country,
            track.previewUrl,
            track.isFavourite
        )
    }

    fun trackMap(track: ParcelableTrack): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.albumName,
            track.releaseYear,
            track.genreName,
            track.country,
            track.previewUrl!!,
            track.isFavourite
        )
    }
}