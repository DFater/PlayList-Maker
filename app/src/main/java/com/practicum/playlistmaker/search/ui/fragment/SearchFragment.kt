package com.practicum.playlistmaker.search.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.ui.activity.PlayerActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.utils.SearchActivityState
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.track.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private val viewModel: SearchViewModel by viewModel()

    private var searchText: String = ""

    private val tracks = ArrayList<Track>()

    private val adapter = TrackAdapter(tracks).apply {
        clickListener = TrackAdapter.TrackClickListener {
            viewModel.addTrackToHistory(it)
            viewModel.showPlayer(it)
        }
    }

    private val historyAdapter = TrackAdapter(ArrayList()).apply {
        clickListener = TrackAdapter.TrackClickListener {
            viewModel.showPlayer(it)
        }
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.getShowPlayerLaunch().observe(viewLifecycleOwner) {
            showPlayerActivity(it)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.isVisible = !s.isNullOrEmpty()
                viewModel.onEditTextChanged(
                    binding.inputEditText.hasFocus(),
                    binding.inputEditText.text.toString()
                )
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = binding.inputEditText.text.toString()
            }
        }


        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText("")
            val imm =
                binding.inputEditText.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.inputEditText.windowToken, 0)
        }

        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onEditorAction()
            }
            false
        }

        binding.inputEditText.addTextChangedListener(simpleTextWatcher)

        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            viewModel.onEditFocusChange(hasFocus)
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel.onClickClearSearchButton()
        }

        binding.buttonRefresh.setOnClickListener {
            viewModel.onButtonRefresh()
        }

        binding.rvTrack.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTrack.adapter = adapter

        val mLayoutManager = LinearLayoutManager(requireContext())
        mLayoutManager.reverseLayout = true
        binding.rvHistoryTrack.layoutManager = mLayoutManager
        binding.rvHistoryTrack.adapter = historyAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.inputEditText.setText(savedInstanceState?.getString(SEARCH_TEXT))
        binding.inputEditText.setSelection(binding.inputEditText.text.length)
    }

    private fun showPlayerActivity(track: Track) {
        findNavController().navigate(R.id.action_searchFragment_to_playerActivity,
            PlayerActivity.createArgs(track))
    }

    private fun render(state: SearchActivityState) {
        binding.rvTrack.isVisible = state is SearchActivityState.List
        binding.placeholderEmpty.isVisible = state is SearchActivityState.Empty
        binding.placeholderError.isVisible = state is SearchActivityState.Error
        binding.historySearchLinearLayout.isVisible = state is SearchActivityState.History
        binding.progressBar.isVisible = state is SearchActivityState.Loading
        when (state) {
            is SearchActivityState.List -> adapter.addTracks(state.tracks)
            is SearchActivityState.Empty -> Unit
            is SearchActivityState.Error -> Unit
            is SearchActivityState.History -> historyAdapter.addTracks(state.tracks)
            is SearchActivityState.Loading -> Unit
        }
    }


}
