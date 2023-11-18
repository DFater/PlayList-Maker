package com.practicum.playlistmaker.media.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavouriteTracksBinding
import com.practicum.playlistmaker.media.ui.models.FavouriteTrackScreenState
import com.practicum.playlistmaker.media.ui.view_model.FavouritesViewModel
import com.practicum.playlistmaker.player.ui.activity.PlayerActivity
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTrackFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteTracksBinding

    private val viewModel: FavouritesViewModel by viewModel()

    private val favouritesAdapter = FavouriteTrackAdapter(ArrayList()).apply {
        clickListener = FavouriteTrackAdapter.TrackClickListener {
            viewModel.showPlayer(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lifecycle.addObserver(viewModel)

        binding = FragmentFavouriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favouriteTrackList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.favouriteTrackList.adapter = favouritesAdapter

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.getShowPlayerTrigger().observe(viewLifecycleOwner) {
            showPlayerActivity(it)
        }
    }

    private fun render(state: FavouriteTrackScreenState) {
        binding.messageImage.isVisible = state is FavouriteTrackScreenState.Empty
        binding.messageText.isVisible = state is FavouriteTrackScreenState.Empty
        binding.favouriteTrackList.isVisible = state is FavouriteTrackScreenState.Content
        binding.progressBar.isVisible = state is FavouriteTrackScreenState.Loading
        when (state) {
            is FavouriteTrackScreenState.Loading, FavouriteTrackScreenState.Empty -> Unit
            is FavouriteTrackScreenState.Content -> favouritesAdapter.addItems(state.tracks)
        }
    }

    private fun showPlayerActivity(track: Track) {
        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_playerActivity,
            PlayerActivity.createArgs(track)
        )
    }

    companion object {
        fun newInstance() = FavouriteTrackFragment()
    }
}