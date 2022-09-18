package com.jsdisco.lilhelper.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jsdisco.lilhelper.data.AppRepository
import com.jsdisco.lilhelper.data.local.models.SettingsIngredient
import kotlinx.coroutines.launch


class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = AppRepository.getRepoInstance(application)

    /** from sharedPreferences */
    val settingsAskDeleteNote = repo.settingsAskDeleteNote
    val settingsAskDeleteDrawing = repo.settingsAskDeleteDrawing
    val settingsLoadImgs = repo.settingsLoadImgs

    /** from roomDB */
    val settingsIngs = repo.settingsIngs

    fun toggleSettingsSwitch(key: String){
        repo.toggleSetting(key)
    }

    fun toggleIngCheckbox(settingsIng: SettingsIngredient){
        viewModelScope.launch{
            repo.toggleSettingsIngCheckbox(settingsIng)
        }
    }
}


