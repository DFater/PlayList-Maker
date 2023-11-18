package com.practicum.playlistmaker.media.ui.view_model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.LocalStorageInteractor
import com.practicum.playlistmaker.media.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.ui.models.NewPlaylistScreenResult
import com.practicum.playlistmaker.media.ui.models.NewPlaylistScreenState
import com.practicum.playlistmaker.search.ui.utils.SingleLiveEvent
import com.practicum.playlistmaker.search.ui.utils.debounce
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.random.Random

open class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val localStorageInteractor: LocalStorageInteractor
) : ViewModel() {
    private val playlist: Playlist = Playlist()

    private val stateLiveData = MutableLiveData<NewPlaylistScreenState>()
    fun observeState(): LiveData<NewPlaylistScreenState> = stateLiveData

    private val resultLiveData = SingleLiveEvent<NewPlaylistScreenResult>()
    fun getResult(): LiveData<NewPlaylistScreenResult> = resultLiveData

    private val addPlaylistTrigger = SingleLiveEvent<Playlist>()
    fun getAddPlaylistTrigger(): LiveData<Playlist> = addPlaylistTrigger

    private var isClickAllowed = true
    private val onPlaylistClickDebounce =
        debounce<Boolean>(CLICK_DEBOUNCE_DELAY_MILLIS, viewModelScope, false) {
            isClickAllowed = it
        }

    init {
        setState(NewPlaylistScreenState.Empty)
    }

    private fun setState(state: NewPlaylistScreenState) {
        stateLiveData.value = state
    }

    private fun setResult(result: NewPlaylistScreenResult) {
        resultLiveData.postValue(result)
    }

    private fun setCurrentState() {
        when {
            !playlist.name.isNullOrEmpty() -> setState(NewPlaylistScreenState.Filled(playlist))
            !playlist.description.isNullOrEmpty() || !playlist.filePath.isNullOrEmpty() -> {
                setState(
                    NewPlaylistScreenState.NotEmpty(playlist)
                )
            }

            else -> {
                setState(NewPlaylistScreenState.Empty)
            }
        }
    }

    fun addPlaylist(aPlaylist: Playlist) {
        viewModelScope.launch {
            try {
                val res = playlistInteractor.addPlaylist(playlist = aPlaylist)
                playlist.id = res
                setResult(NewPlaylistScreenResult.Created(playlist))
            } catch (e: Exception) {
                setResult(NewPlaylistScreenResult.Canceled)
            }

        }
    }

    fun onAddPlaylistClick() {
        if (clickDebounce()) {
            addPlaylistTrigger.value = playlist
        }
    }

    open fun needShowDialog(): Boolean {
        return stateLiveData.value is NewPlaylistScreenState.Filled || stateLiveData.value is NewPlaylistScreenState.NotEmpty
    }

    fun onPlaylistNameChanged(text: String?) {
        playlist.name = text
        setCurrentState()
    }

    fun onPlaylistDescriptionChanged(text: String?) {
        playlist.description = text
        setCurrentState()
    }

    fun onPlaylistImageChanged(uri: Uri?) {
        if (uri == null) {
            playlist.filePath = null
        } else {
            playlist.filePath = "pl" + Random.nextInt() + ".jpg"
        }
        setCurrentState()
    }

    fun onCancelPlaylist() {
        setResult(NewPlaylistScreenResult.Canceled)
    }

    fun saveImageToPrivateStorage(inputStream: InputStream?) {
        val file = playlist.filePath?.let {
            File(
                localStorageInteractor.getImageDirectory(),
                it
            )
        }
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(
                Bitmap.CompressFormat.JPEG,
                COMPRESS_QUALITY_DEGREE, outputStream
            )
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            onPlaylistClickDebounce(true)
        }
        return current
    }

    protected fun setPlaylistValue(value: Playlist) {
        playlist.id = value.id
        playlist.name = value.name
        playlist.description = value.description
        playlist.filePath = value.filePath
        playlist.trackList = value.trackList
        playlist.trackCount = value.trackCount
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val COMPRESS_QUALITY_DEGREE = 30
    }
}