package com.practicum.playlistmaker.media.domain.utils

fun getTrackCountNoun(count: Int): String {
    val lastDigit = count % 100
    if (lastDigit in 11..14)
        return "треков"
    return when (lastDigit % 10) {
        1 -> "трек"
        2, 3, 4 -> "трека"
        else -> "треков"
    }
}