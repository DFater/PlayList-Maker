package com.practicum.playlistmaker.domain.api

interface PlayerRepository {
    fun prepare(previewUrl: String)
    fun start()
    fun pause()
    fun release()
    fun createCurrentPosition(): Int
    fun setOnCompletionListener(onCompletion: ()->Unit)
    fun setOnPreparedListener(onPrepared: () -> Unit)
}