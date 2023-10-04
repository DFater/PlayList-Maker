package com.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TrackInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.utils.SearchActivityState
import com.practicum.playlistmaker.search.ui.utils.SingleLiveEvent
import com.practicum.playlistmaker.search.ui.utils.debounce
import kotlinx.coroutines.launch

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

    private val onTrackSearchDebounce =
        debounce<String?>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) {
            if (it?.isEmpty() == false)
                search(it)
        }

    private val onTrackClickDebounce =
        debounce<Boolean>(CLICK_DEBOUNCE_DELAY, viewModelScope, false) {
            isClickAllowed = it
        }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            onTrackClickDebounce(true)
        }
        return current
    }

    private fun searchDebounce(changedText: String) {
        if (searchTextLast == changedText) {
            return
        }
        this.searchTextLast = changedText
        onTrackSearchDebounce(changedText)
    }

    private fun search(searchText: String) {
        if (searchText.isEmpty()) {
            renderState(SearchActivityState.List(ArrayList()))
            return
        }
        renderState(SearchActivityState.Loading)

        viewModelScope.launch {
            trackInteractor
                .search(searchText)
                .collect {
                    val tracks = ArrayList<Track>()
                    if (it.first != null) {
                        tracks.addAll(it.first!!)
                    }
                    when {
                        it.second != null -> {
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
            }
    }


    fun onButtonRefresh() {
        search(searchTextPresent ?: "")
    }

    fun onEditTextChanged(hasFocus: Boolean, text: String?) {
        searchTextPresent = text
        val tracks = getHistoryTrackList()
        if (hasFocus && searchTextPresent?.isEmpty() == true && tracks.size > 0) {
            onTrackSearchDebounce(searchTextPresent)
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
            onTrackSearchDebounce(searchTextPresent)
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
