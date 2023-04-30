package com.practicum.playlistmaker

import com.google.gson.annotations.SerializedName

data class Tracks(
    @SerializedName("trackName")
    val trackName: String,
    @SerializedName("artistName")
    val actorsName: String,
    @SerializedName("trackTimeMillis")
    val trackDuration: Long,
    @SerializedName("artworkUrl100")
    val imageUrl: String
)