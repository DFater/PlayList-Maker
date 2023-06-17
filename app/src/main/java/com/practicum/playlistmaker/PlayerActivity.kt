package com.practicum.playlistmaker

import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.SearchHistory
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY_MILLIS = 25L
        private const val TIME_FORMAT = "mm:ss"
        private const val START_TIME = "00:00"
    }

    private var playerState = STATE_DEFAULT

    private var mediaPlayer = MediaPlayer()
    private var mainThreadHandler: Handler? = null

    private val arrowMediaBack: ImageView by lazy { findViewById(R.id.arrow_media_back) }
    private val albumCover: ImageView by lazy { findViewById(R.id.album_cover) }
    private val trackNamePlayer: TextView by lazy { findViewById(R.id.track_name_player) }
    private val actorNamePlayer: TextView by lazy { findViewById(R.id.actor_name_player) }
    private val timeDuration: TextView by lazy { findViewById(R.id.time_duration_playing_track) }
    private val albumName: TextView by lazy { findViewById(R.id.album_name_playing_track) }
    private val yearOfRelease: TextView by lazy { findViewById(R.id.year_of_release_playing_track) }
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

        preparePlayer(item)

        playButton.setOnClickListener {
            playbackControl()
        }
        arrowMediaBack.setOnClickListener { finish() }

        trackNamePlayer.text = item.trackName
        actorNamePlayer.text = item.actorsName
        albumName.text = item.collectionName
        yearOfRelease.text = item.releaseDate.substring(0, 4)
        genre.text = item.primaryGenreName
        country.text = item.country
        timeDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackDuration)
        SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(item.trackDuration)

        Glide.with(applicationContext)
            .load(item.imageUrl.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(albumCover)
    }

    private fun preparePlayer(item: Tracks) {
        mediaPlayer.setDataSource(item.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED

        }
    }


    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.pause)
        playerState = STATE_PLAYING
        mainThreadHandler?.post(
            createUpdateProgressTime()
        )
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.play)
        playerState = STATE_PAUSED
    }

    private fun createUpdateProgressTime(): Runnable {
        return object : Runnable {
            override fun run() {
                when (playerState) {
                    STATE_PLAYING -> {
                        timeProgress.text = SimpleDateFormat(
                            TIME_FORMAT,
                            Locale.getDefault()
                        ).format(mediaPlayer.currentPosition)
                        mainThreadHandler?.postDelayed(this, DELAY_MILLIS)
                    }
                    STATE_PAUSED -> {
                        mainThreadHandler?.removeCallbacks(this)
                    }
                    STATE_PREPARED -> {
                        mainThreadHandler?.removeCallbacks(this)
                        playButton.setImageResource(R.drawable.play)
                        timeProgress.text = START_TIME
                    }

                }
            }
        }

    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }


    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
