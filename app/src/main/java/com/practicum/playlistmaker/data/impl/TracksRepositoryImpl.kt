package com.practicum.playlistmaker.data.impl

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.models.Tracks
import java.text.SimpleDateFormat
import java.util.*

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun search(
        expression: String,
        onEmpty: () -> Unit,
        onFailure: () -> Unit
    ): ArrayList<Tracks> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            val list = (response as TracksSearchResponse).results.map {
                Tracks(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis),
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }
            val arrayList = ArrayList<Tracks>()
            arrayList.addAll(list)
            if (arrayList.isEmpty()) {
                onEmpty()
            }
            return arrayList
        } else {
            onFailure()
            return ArrayList()
        }
    }
}
