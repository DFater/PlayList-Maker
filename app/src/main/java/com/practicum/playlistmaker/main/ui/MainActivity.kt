package com.practicum.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.search.ui.fragment.SearchFragment
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.media.ui.fragment.MediaLibraryFragment
import com.practicum.playlistmaker.setting.ui.fragment.SettingsFragment


class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.searchButton?.setOnClickListener {
            val searchIntent = Intent(this@MainActivity, SearchFragment::class.java)
            startActivity(searchIntent)
        }

        binding?.mediaLibraryButton?.setOnClickListener {
            val mediaIntent = Intent(this@MainActivity, MediaLibraryFragment::class.java)
            startActivity(mediaIntent)
        }
        binding?.settingsButton?.setOnClickListener {
            val settingsIntent = Intent(this@MainActivity, SettingsFragment::class.java)
            startActivity(settingsIntent)
        }
    }
}
