package com.jsdisco.lilhelper.ui.settings

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jsdisco.lilhelper.adapter.SettingsAdapter
import com.jsdisco.lilhelper.data.SettingsRepository
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
        val adapter = SettingsAdapter(emptyList(), onCheckBoxClick)
        rvSettings.adapter = adapter

        viewModel.settings.observe(
            viewLifecycleOwner,
            Observer{
                adapter.submitList(it)
            }
        )
    }

    private val onCheckBoxClick = {setting: SettingsIngredient ->
        viewModel.toggleIngCheckbox(setting)
    }
}

/*private val viewModel: SettingsViewModel by lazy {
            val activity = requireNotNull(this.activity){
                "blah"
            }
            ViewModelProvider.of(this, Factory(activity.application))
                .get(SettingsViewModel::class.java)
        }*/

/*        /*val settingsRepo = SettingsRepository()
        val factory = SettingsViewModel.Factory(settingsRepo)
        viewModel = ViewModelProvider(this, factory).get(SettingsViewModel::class.java)*/

        */