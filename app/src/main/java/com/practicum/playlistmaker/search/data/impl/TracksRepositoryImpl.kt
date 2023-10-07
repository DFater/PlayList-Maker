package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.media.data.AppDatabase
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


class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase
) : TrackRepository {

    companion object {
        private const val SUCCESS_RESULT_CODE = 200
        private const val ERROR_MESSAGE = "Server error"
    }

    override fun search(
        expression: String
    ): Flow<Resource<ArrayList<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == SUCCESS_RESULT_CODE) {
            val favouriteTracks = appDatabase.trackDao().getTrackIds()
            val list = (response as TracksSearchResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis),
                    it.artworkUrl100,
                    it.collectionName,
                    getReleaseYear(it.releaseDate),
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl,
                    isFavouriteTrack(it.trackId, favouriteTracks)
                )
            }
            val arrayList = ArrayList<Track>()
            arrayList.addAll(list)
            emit(Resource.Success(arrayList))
        } else {
            emit(Resource.Error(ERROR_MESSAGE))
        }
    }

    private fun getReleaseYear(date: Date?): Int? {
        return if (date != null) {
            (SimpleDateFormat("yyyy", Locale.getDefault()).format(date).orEmpty()).toInt()
        } else {
            null
        }
    }

    private fun isFavouriteTrack(trackId: Long, favouriteTracks: List<Long>): Boolean {
        return favouriteTracks.indexOf(trackId) > -1
    }
}