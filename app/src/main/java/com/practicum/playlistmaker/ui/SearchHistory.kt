package com.practicum.playlistmaker.ui

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.models.Track
import java.lang.reflect.Type
import kotlin.collections.ArrayList

class SearchHistory {

    fun setTrack(track: Track, sharedPreferences: SharedPreferences) {
        val tracks = read(sharedPreferences)
        if (!tracks.remove(track) && tracks.size >= MAX_COUNT_HISTORY_TRACK) tracks.removeAt(
            MAX_COUNT_HISTORY_TRACK - 1)
        tracks.add(0, track)
        write(sharedPreferences, tracks)
    }


    fun read(sharedPreferences: SharedPreferences): MutableList<Track> {
        val json = sharedPreferences.getString(TRACKS_KEY, null) ?: return mutableListOf()
        val listOfMyClassObject: Type = object : TypeToken<ArrayList<Track>?>() {}.type
        return Gson().fromJson(json, listOfMyClassObject)
    }


    fun write(sharedPreferences: SharedPreferences, tracks: MutableList<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(TRACKS_KEY, json)
            .apply()
    }

    fun clearTracksHistory(sharedPreferences: SharedPreferences) {
        sharedPreferences.edit()
            .remove(TRACKS_KEY)
            .apply()
    }

    companion object {
        const val TRACKS_KEY = "track_key"
        const val MAX_COUNT_HISTORY_TRACK = 10
    }
}