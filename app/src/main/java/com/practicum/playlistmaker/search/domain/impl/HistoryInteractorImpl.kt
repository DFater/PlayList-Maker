package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track

class HistoryInteractorImpl(private val historyRepository: HistoryRepository): HistoryInteractor {
    override fun getSearchHistory(): ArrayList<Track> {
        return historyRepository.getSearchHistory()
    }

    override fun addTrackToSearchHistory(track: Track) {
        val tracks = getSearchHistory()
        val trackInd = tracks.indexOf(track)
        if (trackInd > -1) {
            tracks.removeAt(trackInd)
        }
        if (tracks.size > MAX_COUNT_HISTORY_TRACK) {
            tracks.removeAt(0)
        }
        tracks.add(track)
        historyRepository.saveSearchHistory(tracks)
    }

    override fun clearSearchHistory() {
        historyRepository.clearSearchHistory()
    }
    companion object {
        const val MAX_COUNT_HISTORY_TRACK = 9
    }
}