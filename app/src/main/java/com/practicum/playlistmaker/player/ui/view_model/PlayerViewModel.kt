package com.practicum.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.ui.models.PlayerScreenState
import com.practicum.playlistmaker.search.domain.models.Track

class PlayerViewModel(private val track: Track, private val playerInteractor: PlayerInteractor) :
    ViewModel() {

    private val _state = MutableLiveData<PlayerScreenState>()
    private val handler = Handler(Looper.getMainLooper())
    private var currentTime: String? = null

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
        _state.postValue(state)
    }

    private fun runTimerTask(): Runnable {
        return Runnable {
            currentTime = getCurrentTime()
            if (_state.value is PlayerScreenState.Playing || _state.value is PlayerScreenState.Progress) {
                handler.postDelayed(runTimerTask(), TIME_DEBOUNCE_DELAY_MILLIS)
                renderState(PlayerScreenState.Progress(getCurrentTime()))
            }
        }
    }

    private fun onStartPlayer() {
        renderState(PlayerScreenState.Playing())
        handler.post(runTimerTask())
    }

    private fun onPausePlayer() {
        renderState(PlayerScreenState.Paused(currentTime))
        stopTimerTask()
    }

    private fun getCurrentTime(): String? = playerInteractor.getCurrentPosition(DEFAULT_TIME)

    fun playBackControl() {
        playerInteractor.playbackControl({ onStartPlayer() }, { onPausePlayer() })
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer { onPausePlayer() }
    }

    private fun stopTimerTask() {
        handler.removeCallbacks(runTimerTask())
    }

    override fun onCleared() {
        super.onCleared()
        stopTimerTask()
        playerInteractor.release()
    }

    companion object {
        private const val DEFAULT_TIME = "00:00"
        private const val TIME_DEBOUNCE_DELAY_MILLIS = 500L
    }
}