package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.Tracks
import java.lang.reflect.Type

class SearchHistory {

    fun setTrack(track: Tracks, sharedPreferences: SharedPreferences) {
        val tracks = read(sharedPreferences)
        if (!tracks.remove(track) && tracks.size >= MAXIMUM) tracks.removeAt(MAXIMUM - 1)
        tracks.add(0, track)
        write(sharedPreferences, tracks)
    }


    fun read(sharedPreferences: SharedPreferences): MutableList<Tracks> {
        val json = sharedPreferences.getString(TRACKS_KEY, null) ?: return mutableListOf()
        val listOfMyClassObject: Type = object : TypeToken<ArrayList<Tracks>?>() {}.type
        return Gson().fromJson(json, listOfMyClassObject)
    }


    fun write(sharedPreferences: SharedPreferences, tracks: MutableList<Tracks>) {
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
        const val MAXIMUM = 10
    }
}