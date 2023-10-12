package com.practicum.playlistmaker.media.data.entity

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.Companion.INTEGER
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track")
data class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(typeAffinity = INTEGER)
    val id: Int? = null,
    val trackId: Long?,
    val trackName: String?,
    val artistName: String?,
    val trackTime: String?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: Int?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String
)
