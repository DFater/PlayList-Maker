package com.practicum.playlistmaker.player.ui.activity

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.ui.models.ParcelableTrack
import com.practicum.playlistmaker.player.ui.models.PlayerScreenState
import com.practicum.playlistmaker.player.ui.models.TrackMapper
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity() {

    private var binding: ActivityPlayerBinding? = null
    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(track)
    }

    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)

        setContentView(binding?.root)

        track = getCurrentTrack()

        viewModel?.observeState()?.observe(this) {
            render(it)
        }

        viewModel.observeFavourite().observe(this) {
            renderFavourite(it)
        }

        init(track)

        binding?.arrowMediaBack?.setOnClickListener {
            finish()
        }

        binding?.playButton?.setOnClickListener {
            viewModel?.playBackControl()
        }

        binding?.likeButton?.setOnClickListener {
            viewModel.onLikeTrackClick()
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel?.pausePlayer()
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
        with(binding) {
            this?.trackNamePlayer?.text = track.trackName.orEmpty()
            this?.actorNamePlayer?.text = track.artistName.orEmpty()
            this?.timeDurationPlayingTrack?.text = track.trackTime.orEmpty()
            this?.albumNamePlayingTrack?.text = track.albumName.orEmpty()
            this?.countryPlayingTrack?.text = track.country.orEmpty()
            this?.yearOfReleasePlayingTrack?.text = track.releaseYear.toString()
            this?.genrePlayingTrack?.text = track.genreName.orEmpty()
        }
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
            .into(binding?.albumCover!!)
    }

    private fun setTime(time: String?) {
        if (!time.isNullOrEmpty()) {
            binding?.trackTimeProgressInPlayer?.text = time
        }
    }

    private fun onGetDefaultState() {
        binding?.playButton?.isEnabled = false
    }

    private fun onGetPreparedState() {
        binding?.playButton?.setImageResource(R.drawable.play)
        binding?.playButton?.isEnabled = true
    }

    private fun onGetPlayingState() {
        binding?.playButton?.setImageResource(R.drawable.pause)
    }

    private fun onGetProgressState(time: String?) {
        setTime(time)
    }

    private fun onGetPausedState(time: String?) {
        setTime(time)
        binding?.playButton?.setImageResource(R.drawable.play)
    }

    private fun renderFavourite(isFavourite: Boolean) {
        if (isFavourite) {
            binding?.likeButton?.setImageResource(R.drawable.like_button_enable)
        } else {
            binding?.likeButton?.setImageResource(R.drawable.like_button_disable)
        }
    }

    companion object {
        const val TRACK = "Track"

        fun createArgs(track: Track): Bundle =
            bundleOf(TRACK to TrackMapper.trackMap(track))
    }
}