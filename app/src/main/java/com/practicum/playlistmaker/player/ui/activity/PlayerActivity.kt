package com.practicum.playlistmaker.player.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.ui.models.ParcelableTrack
import com.practicum.playlistmaker.player.ui.models.PlayerScreenState
import com.practicum.playlistmaker.player.ui.models.TrackMapper
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val track = getCurrentTrack()
        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.factory(track)
        )[PlayerViewModel::class.java]

        viewModel.observeState().observe(this) {
            render(it)
        }

        init(track)

        binding.arrowMediaBack.setOnClickListener {
            finish()
        }

        binding.playButton.setOnClickListener {
            viewModel.playBackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    private fun getCurrentTrack(): Track {
        val track: ParcelableTrack? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK, ParcelableTrack::class.java)
        } else {
            intent.getParcelableExtra(TRACK)
        }
        return TrackMapper.trackMap(track ?: ParcelableTrack())
    }

    private fun render(state: PlayerScreenState) {
        when (state) {
            is PlayerScreenState.Default -> onGetDefaultState()
            is PlayerScreenState.Prepared -> onGetPreparedState()
            is PlayerScreenState.Playing -> onGetPlayingState()
            is PlayerScreenState.Progress -> onGetProgressState(state.time)
            is PlayerScreenState.Paused -> onGetPausedState(state.time)
        }
    }

    private fun init(track: Track) {
        binding.trackNamePlayer.text = track.trackName.orEmpty()
        binding.actorNamePlayer.text = track.artistName.orEmpty()
        binding.timeDurationPlayingTrack.text = track.trackTime.orEmpty()
        binding.albumNamePlayingTrack.text = track.albumName.orEmpty()
        binding.countryPlayingTrack.text = track.country.orEmpty()
        binding.yearOfReleasePlayingTrack.text = track.getReleaseYear()
        binding.genrePlayingTrack.text = track.genreName.orEmpty()
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(
                RoundedCorners(
                    (resources.getDimension(R.dimen.tiny_size) * (resources
                        .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
                )
            )
            .into(binding.albumCover)

    }

    private fun setTime(time: String?) {
        if (!time.isNullOrEmpty()) {
            binding.trackTimeProgressInPlayer.text = time
        }
    }

    private fun onGetDefaultState() {
        binding.playButton.isEnabled = false
    }

    private fun onGetPreparedState() {
        binding.playButton.setImageResource(R.drawable.play)
        binding.playButton.isEnabled = true
    }

    private fun onGetPlayingState() {
        binding.playButton.setImageResource(R.drawable.pause)
    }

    private fun onGetProgressState(time: String?) {
        setTime(time)
    }

    private fun onGetPausedState(time: String?) {
        setTime(time)
        binding.playButton.setImageResource(R.drawable.play)
    }

    companion object {
        const val TRACK = "Track"
        fun show(context: Context, track: Track) {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(TRACK, TrackMapper.trackMap(track))

            context.startActivity(intent)
        }
    }
}