package com.practicum.playlistmaker.data.dto

data class TracksSearchResponse(
    val searchType: String,
    val expression: String,
    val results: ArrayList<TracksDto>
) : Response()