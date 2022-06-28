package com.jsdisco.lilhelper.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.jsdisco.lilhelper.data.AppRepository
import com.jsdisco.lilhelper.ui.recipes.ApiStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class Status { LOADING, ERROR, DONE }

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = AppRepository.getRepoInstance(application)

    private val _loading = MutableLiveData<Status>()
    val loading: LiveData<Status>
        get() = _loading

    init {
        viewModelScope.launch {
            repo.initSettings(application.applicationContext)
            repo.initRecipeDB()
            _loading.value = Status.DONE
        }
    }
}