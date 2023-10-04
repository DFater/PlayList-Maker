package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*


class TracksRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    companion object {
        private const val SUCCESS_RESULT_CODE = 200
        private const val ERROR_MESSAGE = "Server error"
    }

    override fun search(
        expression: String
    ): Flow<Resource<ArrayList<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == SUCCESS_RESULT_CODE) {
            val list = (response as TracksSearchResponse).results.map {
                Track(
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
            val arrayList = ArrayList<Track>()
            arrayList.addAll(list)
            emit (Resource.Success(arrayList))
        } else {
            emit(Resource.Error(ERROR_MESSAGE))
        }
    }
}