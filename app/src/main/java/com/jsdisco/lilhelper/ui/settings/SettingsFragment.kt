package com.jsdisco.lilhelper.ui.settings

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.jsdisco.lilhelper.adapter.SettingsAdapter
import com.jsdisco.lilhelper.data.models.SettingsIngredient
import com.jsdisco.lilhelper.databinding.FragmentSettingsBinding
//import com.jsdisco.lilhelper.ui.settings.SettingsViewModel.Factory

val Context.dataStore by preferencesDataStore(name = "settings")

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

        viewModel.settingsIngs.observe(
            viewLifecycleOwner,
            Observer{
                adapter.submitList(it)
            }
        )

        Log.e("SettingsFragment", (viewModel.settingsAskDeleteNote.value == true).toString())

        binding.switchSettingsNotes.isChecked = viewModel.settingsAskDeleteNote.value == true
        binding.switchSettingsLists.isChecked = viewModel.settingsAskDeleteList.value == true

        binding.switchSettingsNotes.setOnClickListener{
            viewModel.toggleSettingsSwitch("prefDeleteNote")
        }
        binding.switchSettingsLists.setOnClickListener {
            viewModel.toggleSettingsSwitch("prefDeleteList")
        }
    }

    private val onIngCheckBoxClick = {setting: SettingsIngredient ->
        viewModel.toggleIngCheckbox(setting)
    }
}
