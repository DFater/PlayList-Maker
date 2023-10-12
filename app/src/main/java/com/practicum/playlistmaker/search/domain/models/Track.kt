package com.practicum.playlistmaker.search.domain.models

data class Track(
    val trackId: Long?,
    val trackName: String?,
    val artistName: String?,
    val trackTime: String?,
    val artworkUrl100: String?,
    val albumName: String?,
    val releaseYear: Int?,
    val genreName: String?,
    val country: String?,
    val previewUrl: String,
    var isFavourite: Boolean = false,
) {
    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
}