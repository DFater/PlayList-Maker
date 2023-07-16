package com.practicum.playlistmaker.ui.player

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.PRACTICUM_PREFERENCES
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.SearchHistory
import java.util.*

class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val DELAY_MILLIS = 25L
    }

    private val player = Creator.providePlayerInteractorImpl()

    private var mainThreadHandler: Handler? = null

    private val arrowMediaBack: ImageView by lazy { findViewById(R.id.arrow_media_back) }
    private val albumCover: ImageView by lazy { findViewById(R.id.album_cover) }
    private val trackNamePlayer: TextView by lazy { findViewById(R.id.track_name_player) }
    private val actorNamePlayer: TextView by lazy { findViewById(R.id.actor_name_player) }
    private val timeDuration: TextView by lazy { findViewById(R.id.time_duration_playing_track) }
    private val albumName: TextView by lazy { findViewById(R.id.album_name_playing_track) }
    private val releaseDate: TextView by lazy { findViewById(R.id.year_of_release_playing_track) }
    private val genre: TextView by lazy { findViewById(R.id.genre_playing_track) }
    private val country: TextView by lazy { findViewById(R.id.country_playing_track) }
    private val playButton: ImageButton by lazy { findViewById(R.id.play_button) }
    private val timeProgress: TextView by lazy { findViewById(R.id.track_time_progress_in_player) }
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
        mainThreadHandler = Handler(Looper.getMainLooper())

        player.preparePlayer(item.previewUrl, {
            playButton.setImageResource(R.drawable.play)
            playButton.isEnabled = true
        }, {
            onPausePlayer()
        }, {
            playButton.setImageResource(R.drawable.pause)
            playButton.isEnabled = false
        }
        )

        playButton.setOnClickListener {
            player.playbackControl({ onStartPlayer() }, { onPausePlayer() })
        }
        arrowMediaBack.setOnClickListener { finish() }

        trackNamePlayer.text = item.trackName
        actorNamePlayer.text = item.artistName
        albumName.text = item.albumName
        releaseDate.text = item.getReleaseYear()
        genre.text = item.genreName
        country.text = item.country
        timeDuration.text = item.trackTime

        Glide.with(applicationContext)
            .load(item.getCoverArtwork())
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(albumCover)
    }

    private fun onStartPlayer() {
        playButton.setImageResource(R.drawable.pause)
        mainThreadHandler?.post(
            runTimerTask()
        )
    }

    private fun onPausePlayer() {
        playButton.setImageResource(R.drawable.play)
        mainThreadHandler?.removeCallbacks(runTimerTask())
    }

    private fun setTime() {
        val currentTime = player.createCurrentPosition(getString(R.string.start_time))
        if (!currentTime.isNullOrEmpty()) {
            timeProgress.text = currentTime
        }
    }

    override fun onPause() {
        super.onPause()
        player.pausePlayer { onPausePlayer() }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    private fun runTimerTask(): Runnable {
        return Runnable {
            setTime()
            mainThreadHandler?.postDelayed(runTimerTask(), DELAY_MILLIS)
        }
    }
}
