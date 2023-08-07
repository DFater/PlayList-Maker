package com.practicum.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TrackInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.SearchActivityState
import com.practicum.playlistmaker.search.ui.SingleLiveEvent

class SearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val stateLiveData = MutableLiveData<SearchActivityState>()
    fun observeState(): LiveData<SearchActivityState> = stateLiveData

    private val showPlayerLaunch = SingleLiveEvent<Track>()
    fun getShowPlayerLaunch(): LiveData<Track> = showPlayerLaunch

    private var searchTextLast: String? = null
    private var searchTextPresent: String? = null
    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        val searchTextNew = searchTextLast ?: ""
        search(searchTextNew)
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(searchRunnable)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce(changedText: String) {
        if (searchTextLast == changedText) {
            return
        }
        this.searchTextLast = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun search(searchText: String) {
        if (searchText.isEmpty()) {
            renderState(SearchActivityState.List(ArrayList()))
            return
        }
        renderState(SearchActivityState.Loading)

        trackInteractor.search(searchText, object : TrackInteractor.TrackConsumer {
            override fun consume(foundTracks: ArrayList<Track>?, errorMessage: String?) {
                val tracks = ArrayList<Track>()
                if (foundTracks != null) {
                    tracks.addAll(foundTracks)
                }

                when {
                    errorMessage != null -> {
                        renderState(SearchActivityState.Error)
                    }

                    tracks.isEmpty() -> {
                        renderState(SearchActivityState.Empty)
                    }

                    else -> {
                        renderState(SearchActivityState.List(tracks = tracks))
                    }
                }
            }
        })
    }


    fun onButtonRefresh() {
        search(searchTextPresent ?: "")
    }

    fun onEditTextChanged(hasFocus: Boolean, text: String?) {
        searchTextPresent = text
        val tracks = getHistoryTrackList()
        if (hasFocus && searchTextPresent?.isEmpty() == true && tracks.size > 0) {
            handler.removeCallbacks(searchRunnable)
            renderState(SearchActivityState.History(tracks))
        } else {
            searchDebounce(searchTextPresent ?: "")
            renderState(
                SearchActivityState.List(
                    if (stateLiveData.value is SearchActivityState.List) {
                        (stateLiveData.value as SearchActivityState.List).tracks
                    } else {
                        ArrayList()
                    }
                )
            )
        }
    }

    fun onEditFocusChange(hasFocus: Boolean) {
        val tracks = getHistoryTrackList()
        if (hasFocus && searchTextPresent.isNullOrEmpty() && tracks.size > 0) {
            renderState(SearchActivityState.History(tracks))
        } else {
            renderState(
                SearchActivityState.List(
                    if (stateLiveData.value is SearchActivityState.List) {
                        (stateLiveData.value as SearchActivityState.List).tracks
                    } else {
                        ArrayList()
                    }
                )
            )
        }
    }

    fun onClickClearSearchButton() {
        clearHistoryTrackList()
        renderState(SearchActivityState.List(ArrayList()))
    }

    fun onEditorAction() {
        search(searchTextPresent ?: "")
    }

    fun showPlayer(track: Track) {
        if (clickDebounce()) {
            showPlayerLaunch.value = track
        }
    }

    fun addTrackToHistory(track: Track) {
        historyInteractor.addTrackToSearchHistory(track)
    }

    private fun getHistoryTrackList(): ArrayList<Track> {
        return historyInteractor.getSearchHistory()
    }

    private fun clearHistoryTrackList() {
        historyInteractor.clearSearchHistory()
    }

    private fun renderState(state: SearchActivityState) {
        stateLiveData.postValue(state)
    }
}
