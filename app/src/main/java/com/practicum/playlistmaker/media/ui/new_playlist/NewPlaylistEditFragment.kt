package com.practicum.playlistmaker.media.ui.new_playlist

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.ui.convertor.ParcelablePlaylistConvertor
import com.practicum.playlistmaker.media.ui.models.NewPlaylistScreenResult
import com.practicum.playlistmaker.media.ui.view_model.NewPlaylistEditViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

import java.io.File

class NewPlaylistEditFragment : NewPlaylistFragment() {

    private val args: NewPlaylistEditFragmentArgs by navArgs()

    override val viewModel: NewPlaylistEditViewModel by viewModel {

        parametersOf(ParcelablePlaylistConvertor.convert(args.playlist))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewAttributes()

        val playlist = ParcelablePlaylistConvertor.convert(args.playlist)
        initPlaylistData(playlist)
    }
    private fun initPlaylistData(playlist: Playlist) {
        binding.edPlaylistName.editText?.setText(playlist.name)
        binding.edPlaylistDescription.editText?.setText(playlist.description)
        binding.playlistImageView.setImageResource(R.drawable.no_image_playlist)
        if (!playlist.filePath.isNullOrEmpty()) {
            val file = playlist.filePath?.let { File(viewModel.getImageDirectory(), it) }
            binding.playlistImageView.setImageURI(Uri.fromFile(file))
            binding.playlistImageView.clipToOutline = true
        }
    }

    private fun setViewAttributes() {
        binding.btCreatePlaylist.setText(R.string.button_save_playlist)
        binding.tvPlaylistTitle.setText(R.string.button_edit_playlist)
    }

    override fun renderResult(result: NewPlaylistScreenResult) {
        when (result) {
            is NewPlaylistScreenResult.Canceled -> privateNavigateUp()
            is NewPlaylistScreenResult.Created -> privateNavigateUp()
        }
    }
}