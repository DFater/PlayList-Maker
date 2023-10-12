package com.practicum.playlistmaker.media.ui.view_holder

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

class FavouriteTrackViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.track_item, parentView, false)
) {

    private val trackNameView: TextView by lazy { itemView.findViewById(R.id.track_name) }
    private val trackActorView: TextView by lazy { itemView.findViewById(R.id.actors_name) }
    private val trackTimeDurView: TextView by lazy { itemView.findViewById(R.id.track_duration) }
    private val trackImageView: ImageView by lazy { itemView.findViewById(R.id.track_image) }

    fun bind(model: Track) {
        trackNameView.text = model.trackName
        trackActorView.text = model.artistName
        trackTimeDurView.text = model.trackTime

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
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