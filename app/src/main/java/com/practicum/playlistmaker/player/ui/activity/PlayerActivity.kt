package com.practicum.playlistmaker.player.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.ui.models.ParcelableTrack
import com.practicum.playlistmaker.player.ui.models.PlayerScreenState
import com.practicum.playlistmaker.player.ui.models.TrackMapper
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.android.ext.android.getKoin
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity() {
    private var binding: ActivityPlayerBinding? = null
    private var viewModel: PlayerViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)

        setContentView(binding?.root)

        val track = getCurrentTrack()

        viewModel = getKoin().get { parametersOf(track) }

        viewModel?.observeState()?.observe(this) {
            render(it)
        }

        init(track)

        binding?.arrowMediaBack?.setOnClickListener {
            finish()
        }

        binding?.playButton?.setOnClickListener {
            viewModel?.playBackControl()
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
            this?.yearOfReleasePlayingTrack?.text = track.getReleaseYear()
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

    companion object {
        const val TRACK = "Track"
        fun show(context: Context, track: Track) {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(TRACK, TrackMapper.trackMap(track))

            context.startActivity(intent)
        }
    }
}