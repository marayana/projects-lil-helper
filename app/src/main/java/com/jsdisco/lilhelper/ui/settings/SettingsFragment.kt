package com.jsdisco.lilhelper.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jsdisco.lilhelper.adapter.SettingsAdapter
import com.jsdisco.lilhelper.data.local.models.SettingsIngredient
import com.jsdisco.lilhelper.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var binding: FragmentSettingsBinding

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

        val rvSettings = binding.rvSettingsIngs
        val adapter = SettingsAdapter(emptyList(), onIngCheckBoxClick)
        rvSettings.adapter = adapter

        viewModel.settingsIngs.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.settingsAskDeleteNote.observe(viewLifecycleOwner) {
            binding.switchSettingsNotes.isChecked = it
        }
        viewModel.settingsAskDeleteDrawing.observe(viewLifecycleOwner) {
            binding.switchSettingsDrawings.isChecked = it
        }
        viewModel.settingsLoadImgs.observe(viewLifecycleOwner) {
            binding.switchSettingsImgs.isChecked = it
        }

        binding.switchSettingsNotes.setOnClickListener {
            viewModel.toggleSettingsSwitch("prefDeleteNote")
        }
        binding.switchSettingsDrawings.setOnClickListener {
            viewModel.toggleSettingsSwitch("prefDeleteDrawing")
        }
        binding.switchSettingsImgs.setOnClickListener {
            viewModel.toggleSettingsSwitch("prefLoadImgs")
        }
    }

    private val onIngCheckBoxClick = { setting: SettingsIngredient ->
        viewModel.toggleIngCheckbox(setting)
    }
}
