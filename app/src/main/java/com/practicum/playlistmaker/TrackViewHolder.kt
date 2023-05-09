package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.*

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
    private val trackImage: ImageView by lazy { itemView.findViewById(R.id.track_image) }
    private val trackName: TextView by lazy { itemView.findViewById(R.id.track_name)}
    private val actorsName: TextView by lazy { itemView.findViewById(R.id.actors_name)}
    private val trackDuration: TextView by lazy { itemView.findViewById(R.id.track_duration)}

    fun bind(model: Tracks) {
        trackName.text = model.trackName
        actorsName.text = model.actorsName
        trackDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackDuration)
        Glide.with(itemView)
            .load(model.imageUrl)
            .placeholder(R.drawable.no_image)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(trackImage)
    }
}