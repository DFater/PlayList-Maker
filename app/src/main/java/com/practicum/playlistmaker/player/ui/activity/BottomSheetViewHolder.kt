package com.practicum.playlistmaker.player.ui.activity

import android.net.Uri
import android.os.Environment
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistTrackItemBinding
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.utils.getTrackCountNoun
import com.practicum.playlistmaker.media.ui.new_playlist.NewPlaylistFragment
import java.io.File

class BottomSheetViewHolder(private val binding: PlaylistTrackItemBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(model: Playlist) {
        binding.playlistListItemName.text = model.name.toString()
        binding.playlistListItemTrackCount.text =
            String.format("%d %s", model.trackCount, getTrackCountNoun(model.trackCount))

        binding.playlistListItemImage.setImageResource(R.drawable.no_image_playlist)
        if (!model.filePath.isNullOrEmpty()) {
            val filePath = File(
                itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                NewPlaylistFragment.IMAGE_DIR
            )
            val file = File(filePath, model.filePath!!)
            binding.playlistListItemImage.setImageURI(Uri.fromFile(file))
            binding.playlistListItemImage.clipToOutline = true
        }
    }
}