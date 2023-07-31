package com.practicum.playlistmaker.search.data.dto

import java.util.*

data class TracksDto(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: Date,
    val primaryGenreName: String,
    val country: String?,
    val previewUrl: String
)