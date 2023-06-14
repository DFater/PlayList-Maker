package com.practicum.playlistmaker

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.SearchHistory
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

    private val arrowMediaBack: ImageView by lazy { findViewById(R.id.arrow_media_back) }
    private val albumCover: ImageView by lazy { findViewById(R.id.album_cover) }
    private val trackNamePlayer: TextView by lazy { findViewById(R.id.track_name_player) }
    private val actorNamePlayer: TextView by lazy { findViewById(R.id.actor_name_player) }
    private val timeDuration: TextView by lazy { findViewById(R.id.time_duration_playing_track) }
    private val albumName: TextView by lazy { findViewById(R.id.album_name_playing_track) }
    private val yearOfRelease: TextView by lazy { findViewById(R.id.year_of_release_playing_track) }
    private val genre: TextView by lazy { findViewById(R.id.genre_playing_track) }
    private val country: TextView by lazy { findViewById(R.id.country_playing_track) }
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(
            PRACTICUM_PREFERENCES,
            MODE_PRIVATE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        val searchHistory = SearchHistory()

        val item = searchHistory.read(sharedPreferences)[0]


        arrowMediaBack.setOnClickListener { finish() }

        trackNamePlayer.text = item.trackName
        actorNamePlayer.text = item.actorsName
        albumName.text = item.collectionName
        yearOfRelease.text = item.releaseDate.substring(0, 4)
        genre.text = item.primaryGenreName
        country.text = item.country
        timeDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackDuration)

        Glide.with(applicationContext)
            .load(item.imageUrl.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(albumCover)
    }
}
