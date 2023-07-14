package com.practicum.playlistmaker.ui.track

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Tracks
import java.text.SimpleDateFormat
import java.util.*

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
    private val trackImage: ImageView by lazy { itemView.findViewById(R.id.track_image) }
    private val trackName: TextView by lazy { itemView.findViewById(R.id.track_name)}
    private val actorsName: TextView by lazy { itemView.findViewById(R.id.actors_name)}
    private val trackDuration: TextView by lazy { itemView.findViewById(R.id.track_duration)}

    fun bind(model: Tracks) {
        trackName.text = model.trackName
        actorsName.text = model.artistName
        trackDuration.text = model.trackTime
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.no_image)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(trackImage)
    }
}