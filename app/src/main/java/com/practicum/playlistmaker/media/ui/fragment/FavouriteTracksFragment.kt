package com.practicum.playlistmaker.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FavouriteTracksFragmentBinding
import com.practicum.playlistmaker.media.ui.view_model.FavouriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTracksFragment : Fragment() {

    private val viewModel by viewModel<FavouriteTracksViewModel>()

    companion object {
        fun newInstance() = FavouriteTracksFragment().apply {
        }
    }

    private var binding: FavouriteTracksFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FavouriteTracksFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }
}