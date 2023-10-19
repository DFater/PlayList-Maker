package com.practicum.playlistmaker.media.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.media.ui.favourites.FavouriteTrackFragment
import com.practicum.playlistmaker.media.ui.playlists.PlaylistsFragment

class MediaLibraryViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return TAB_NUMBER
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavouriteTrackFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }

    companion object {
        private const val TAB_NUMBER = 2
    }
}