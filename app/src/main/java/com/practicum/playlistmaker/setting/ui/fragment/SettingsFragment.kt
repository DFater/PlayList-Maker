package com.practicum.playlistmaker.setting.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.setting.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeDarkTheme().observe(viewLifecycleOwner) {
            binding.themeSwitcher.isChecked = viewModel.observeDarkTheme().value == true
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchTheme(isChecked)
        }

        binding.viewShare.setOnClickListener {
            viewModel.clickOnShareApp()
        }

        binding.viewService.setOnClickListener {
            viewModel.clickOnServiceButton()
        }

        binding.viewUserAgreement.setOnClickListener {
            viewModel.clickOnTermsOfUseButton()
        }
    }
}