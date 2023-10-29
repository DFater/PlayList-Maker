package com.practicum.playlistmaker.media.ui.playlist_info

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track

class TrackListBottomSheetViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.track_item, parentView, false)
) {

    private val trackNameView: TextView by lazy { itemView.findViewById(R.id.track_name) }
    private val trackInfo: TextView by lazy { itemView.findViewById(R.id.track_info) }
    private val trackImageView: ImageView by lazy { itemView.findViewById(R.id.track_image) }

    fun bind(model: Track) {
        trackNameView.text = model.trackName
        trackInfo.text = itemView.context.getString(
            R.string.playlist_statistics,
            model.artistName,
            model.getTrackTime()
        )
        Glide.with(itemView)
            .load(model.getCoverArtwork60())
            .placeholder(R.drawable.no_image)
            .centerCrop()
            .transform(
                RoundedCorners(
                    (itemView.resources.getDimension(R.dimen.radius_corner) * (itemView.resources
                        .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
                )
            )
            .into(trackImageView)
    }
}