package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.player.domain.models.PlayerState
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class PlayerInteractorImpl(private val mediaPlayerRepository: PlayerRepository) :
    PlayerInteractor {
    private var playerState = PlayerState.STATE_DEFAULT

    override fun preparePlayer(
        previewUrl: String?,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit,
        onError: () -> Unit
    ) {
        mediaPlayerRepository.setOnPreparedListener {
            onPreparedListener()
            playerState = PlayerState.STATE_PREPARED
        }
        try {
            mediaPlayerRepository.prepare(previewUrl.toString())
        } catch (e: Exception) {
            onError()
            return
        }
        mediaPlayerRepository.setOnCompletionListener {
            onCompletionListener()
            playerState = PlayerState.STATE_PREPARED
        }
    }

    override fun startPlayer(onStart: () -> Unit) {
        if (playerState != PlayerState.STATE_DEFAULT) {
            mediaPlayerRepository.start()
            playerState = PlayerState.STATE_PLAYING
            onStart()
        }
    }

    override fun pausePlayer(onPause: () -> Unit) {
        if (playerState != PlayerState.STATE_DEFAULT) {
            onPause()
            mediaPlayerRepository.pause()
            playerState = PlayerState.STATE_PAUSED
        }
    }

    override fun playbackControl(onStart: () -> Unit, onPause: () -> Unit) {
        when (playerState) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer(onPause)
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer(onStart)
            }

            else -> {}
        }
    }

    override fun getCurrentPosition(default: String?): String? {
        return when (playerState) {
            PlayerState.STATE_PLAYING -> SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                mediaPlayerRepository.getCurrentPosition()
            )

            PlayerState.STATE_PREPARED, PlayerState.STATE_DEFAULT -> default
            else -> null
        }
    }

    override fun release() {
        mediaPlayerRepository.release()
    }
}