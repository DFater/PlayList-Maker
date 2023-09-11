package com.practicum.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.ui.models.PlayerScreenState
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(private val track: Track, private val playerInteractor: PlayerInteractor) :
    ViewModel() {

    private val _state = MutableLiveData<PlayerScreenState>()
    private var currentTime: String? = null
    private var timerJob: Job? = null

    init {
        loadPlayer()
    }

    fun observeState(): LiveData<PlayerScreenState> = _state

    private fun loadPlayer() {
        renderState(PlayerScreenState.Default(track))
        playerInteractor.preparePlayer(track.previewUrl, {
            renderState(PlayerScreenState.Prepared)
        }, {
            renderState(PlayerScreenState.Paused(DEFAULT_TIME))
        }, {

        })
    }

    private fun renderState(state: PlayerScreenState) {
        _state.value = state
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (_state.value is PlayerScreenState.Playing || _state.value is PlayerScreenState.Progress) {
                currentTime = getCurrentTime()
                renderState(PlayerScreenState.Progress(getCurrentTime()))
                delay(TIME_DEBOUNCE_DELAY_MILLIS)
            }
        }
    }

    private fun onStartPlayer() {
        renderState(PlayerScreenState.Playing())
        startTimer()
    }

    private fun onPausePlayer() {
        renderState(PlayerScreenState.Paused(currentTime))
        timerJob?.cancel()
    }

    private fun getCurrentTime(): String? = playerInteractor.getCurrentPosition(DEFAULT_TIME)

    fun playBackControl() {
        playerInteractor.playbackControl({ onStartPlayer() }, { onPausePlayer() })
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer { onPausePlayer() }
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
    }

    companion object {
        private const val DEFAULT_TIME = "00:00"
        private const val TIME_DEBOUNCE_DELAY_MILLIS = 300L
    }
}