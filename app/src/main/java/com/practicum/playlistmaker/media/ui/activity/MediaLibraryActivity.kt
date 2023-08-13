package com.practicum.playlistmaker.media.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.practicum.playlistmaker.media.ui.adapter.MediaLibraryViewPagerAdapter

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mediaArrowBack.setOnClickListener {
            finish()
        }

        binding.mediaViewPager.adapter =
            MediaLibraryViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator =
            TabLayoutMediator(binding.mediaTabLayout, binding.mediaViewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.favourites_tracks_tab)
                    1 -> tab.text = getString(R.string.playlists_tab)
                }
            }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}