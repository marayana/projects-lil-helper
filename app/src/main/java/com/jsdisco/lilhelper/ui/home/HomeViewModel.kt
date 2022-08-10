package com.jsdisco.lilhelper.ui.home

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.jsdisco.lilhelper.data.AppRepository
import com.jsdisco.lilhelper.ui.recipes.ApiStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class Status { LOADING, ERROR, DONE }

/**
 * On App Start, the init block triggers the initialisation of settings and recipes:
 *
 *   - settings from sharedPreferences,
 *   - recipes from roomDB (or, if roomDB is empty, the repo attempts to fetch recipes from API)
 *
 * */
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = AppRepository.getRepoInstance(application)

    private val _loading = MutableLiveData<Status>()
    val loading: LiveData<Status>
        get() = _loading

    init {
        initValues(application.applicationContext)
    }

    private fun initValues(context: Context){
        viewModelScope.launch {
            _loading.value = Status.LOADING
            try {
                repo.initSettings(context)
                repo.initRecipeDB()
                _loading.value = Status.DONE
            } catch(e: Exception){
                Log.e("HomeViewModel", "Error initialising settings and recipes: $e")
                _loading.value = Status.ERROR
            }
        }
    }
}