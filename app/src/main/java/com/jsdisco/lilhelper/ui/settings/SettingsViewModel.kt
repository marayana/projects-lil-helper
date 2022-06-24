package com.jsdisco.lilhelper.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jsdisco.lilhelper.data.AppRepository
import com.jsdisco.lilhelper.data.models.SettingsIngredient
import kotlinx.coroutines.launch


class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = AppRepository.getRepoInstance(application)

    val settingsIngs = repo.settingsIngs

    fun toggleIngCheckbox(settingsIng: SettingsIngredient){
        viewModelScope.launch{
            repo.toggleSettingsIngCheckbox(settingsIng)
        }
    }
}



/*
class Factory(
        val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return SettingsViewModel(application) as T
            }
            throw IllegalArgumentException("")
        }
    }


class SettingsViewModelFactory(
    private val repo: SettingsRepository,
    private val database: AppDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(repo) as T
        }
        throw IllegalArgumentException("")
    }
}
*/


/*
class SettingsViewModel(application: Application, dataStore: DataStore<Preferences>) : AndroidViewModel(application) {

    class Factory(private val repo: SettingsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SettingsViewModel(repo) as T
        }
    }

    private val database = getDatabase(application)
    private val repo = SettingsRepository(database, dataStore)

    val settings = repo.settings

    init {
        Log.e("SettingsViewModel", "init")
        loadSettings()
    }

    private fun loadSettings(){
        viewModelScope.launch{
            try {
                repo.initSettings()
            } catch(e: Exception){
                Log.e("SettingsViewModel", "Error loading settings: $e")
            }
        }
    }
}
*/

