package com.practicum.playlistmaker.search.data.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track
import java.lang.reflect.Type

class HistoryRepositoryImpl(private val sharedPreferences: SharedPreferences, private val gson: Gson) : HistoryRepository {
    override fun getSearchHistory(): ArrayList<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_TRACKS, null) ?: return ArrayList()
        val typeOfTrackList: Type = object : TypeToken<ArrayList<Track?>?>() {}.type
        return gson.fromJson(json, typeOfTrackList)
    }

    override fun saveSearchHistory(trackList: ArrayList<Track>) {
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_TRACKS, createJsonFromTrackList(trackList.toTypedArray()))
            .apply()
    }

    override fun clearSearchHistory() {
        sharedPreferences.edit {
            remove(SEARCH_HISTORY_TRACKS)
        }
    }

    private fun createJsonFromTrackList(trackList: Array<Track>): String {
        return gson.toJson(trackList)
    }

    companion object {
        const val SEARCH_HISTORY_TRACKS = "search_history_tracks"
    }
}