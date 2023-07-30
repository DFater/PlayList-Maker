package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface HistoryRepository {
    fun getSearchHistory() : ArrayList<Track>

    fun saveSearchHistory(trackList: ArrayList<Track>)

    fun clearSearchHistory()
}