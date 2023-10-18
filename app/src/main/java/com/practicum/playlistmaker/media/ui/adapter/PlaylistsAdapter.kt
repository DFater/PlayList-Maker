package com.practicum.playlistmaker.media.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.ui.view_holder.PlaylistsViewHolder

class PlaylistsAdapter(private val items: ArrayList<Playlist>) :
    RecyclerView.Adapter<PlaylistsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        return PlaylistsViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun addItems(values: List<Playlist>) {
        this.items.clear()
        if (values.isNotEmpty()) {
            this.items.addAll(values)
        }
        this.notifyDataSetChanged()
    }
}