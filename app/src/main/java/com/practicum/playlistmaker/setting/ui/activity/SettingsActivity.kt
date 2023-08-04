package com.practicum.playlistmaker.setting.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.setting.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.observeDarkTheme().observe(this) {
            binding.themeSwitcher.isChecked = viewModel.observeDarkTheme().value == true
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchTheme(isChecked)
        }

        binding.arrowBack.setOnClickListener {
            finish()
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