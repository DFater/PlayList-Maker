package com.practicum.playlistmaker.player.ui.activity

import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.domain.utils.getTrackCountNoun
import com.practicum.playlistmaker.media.ui.fragment.NewPlaylistFragment
import java.io.File

class BottomSheetViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.playlist_track_item, parentView, false)
) {
    private val playlistImageView: ImageView by lazy { itemView.findViewById(R.id.playlistListItemImage) }
    private val playlistNameView: TextView by lazy { itemView.findViewById(R.id.playlistListItemName) }
    private val playlistTrackInfoView: TextView by lazy { itemView.findViewById(R.id.playlistListItemTrackCount) }

    fun bind(model: Playlist) {
        playlistNameView.text = model.name.toString()
        playlistTrackInfoView.text =
            String.format("%d %s", model.trackCount, getTrackCountNoun(model.trackCount))

        playlistImageView.setImageResource(R.drawable.no_image_playlist)
        if (!model.filePath.isNullOrEmpty()) {
            val filePath = File(
                itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                NewPlaylistFragment.IMAGE_DIR
            )
            val file = File(filePath, model.filePath!!)
            playlistImageView.setImageURI(Uri.fromFile(file))
            playlistImageView.clipToOutline = true
        }
    }
}