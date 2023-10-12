package com.practicum.playlistmaker.media.ui.view_model

import androidx.lifecycle.*
import com.practicum.playlistmaker.media.domain.api.FavouriteTrackInteractor
import com.practicum.playlistmaker.media.ui.FavouriteTrackScreenState
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.utils.SingleLiveEvent
import com.practicum.playlistmaker.search.ui.utils.debounce
import kotlinx.coroutines.launch

class FavouritesViewModel(private val favouritesInteractor: FavouriteTrackInteractor) : ViewModel(),
    DefaultLifecycleObserver {

    private val stateLiveData = MutableLiveData<FavouriteTrackScreenState>()
    fun observeState(): LiveData<FavouriteTrackScreenState> = stateLiveData

    private val showPlayerTrigger = SingleLiveEvent<Track>()
    fun getShowPlayerTrigger(): LiveData<Track> = showPlayerTrigger

    private var isClickAllowed = true
    private val onTrackClickDebounce =
        debounce<Boolean>(CLICK_DEBOUNCE_DELAY, viewModelScope, false) {
            isClickAllowed = it
        }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        loadContent()
    }

    private fun setState(state: FavouriteTrackScreenState) {
        stateLiveData.postValue(state)
    }

    private fun loadContent() {
        setState(FavouriteTrackScreenState.Loading)
        viewModelScope.launch {
            favouritesInteractor
                .getFavouriteTracks()
                .collect {
                    processResult(it)
                }
        }
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            setState(FavouriteTrackScreenState.Empty)
        } else {
            setState(FavouriteTrackScreenState.Content(tracks))
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            onTrackClickDebounce(true)
        }
        return current
    }

    fun showPlayer(track: Track) {
        if (clickDebounce()) {
            showPlayerTrigger.value = track
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}