package com.practicum.playlistmaker.media.data

import com.practicum.playlistmaker.media.data.entity.PlaylistTrackEntity
import com.practicum.playlistmaker.search.domain.models.Track

class PlaylistTrackDbConvertor{

    fun map(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(
            trackId = track.trackId ?: 0,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime= track.trackTime,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.albumName,
            releaseYear = track.releaseYear,
            primaryGenreName = track.genreName,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }
}