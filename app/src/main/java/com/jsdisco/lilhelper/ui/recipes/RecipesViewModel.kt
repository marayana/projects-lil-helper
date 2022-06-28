package com.jsdisco.lilhelper.ui.recipes

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.jsdisco.lilhelper.data.AppRepository
import com.jsdisco.lilhelper.data.models.relations.RecipeWithIngredients
import kotlinx.coroutines.launch

enum class ApiStatus { LOADING, ERROR, DONE }

class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = AppRepository.getRepoInstance(application)

    val recipes = repo.recipes
    val settingsIngs = repo.settingsIngs

    private val _loading = MutableLiveData<ApiStatus>()
    val loading: LiveData<ApiStatus>
        get() = _loading

    fun downloadRecipesFromApi(){
        viewModelScope.launch {
            _loading.value = ApiStatus.LOADING
            try {
                repo.getRecipesFromApi()
                _loading.value = ApiStatus.DONE
            } catch(e: Exception){
                Log.e("RecipesViewModel", "Error downloading recipes from API: $e")
                _loading.value = ApiStatus.ERROR
            }
        }
    }

    fun filterRecipes(by: String) : List<RecipeWithIngredients>? {
        val filtered = recipes.value?.filter{recipe ->
            when(by){
                "warm" -> recipe.recipe.r_cat_warm
                "cold" -> recipe.recipe.r_cat_cold
                "salad" -> recipe.recipe.r_cat_salad
                "soup" -> recipe.recipe.r_cat_soup
                "base" -> recipe.recipe.r_cat_base
                else -> true
            }
        }
        return filtered
    }
/*
    private fun loadRecipes(){
        viewModelScope.launch {
            _loading.value = ApiStatus.LOADING
            try {
                repo.getRecipesFromDb()
            } catch(e: Exception){
                Log.e("RecipesViewModel", "Error loading recipes from database: $e")
                _loading.value = ApiStatus.ERROR
            }
            if (recipes.value.isNullOrEmpty()){
                try {
                    repo.getRecipesFromApi()
                    //repo.initSettingsIngs()
                    _loading.value = ApiStatus.DONE
                } catch(e: Exception){
                    Log.e("RecipesViewModel", "Error fetching recipes from API: $e")
                    _loading.value = ApiStatus.ERROR
                }
            } else {
                _loading.value = ApiStatus.DONE
            }

            loadExcludedIngredients()
        }
    }

    private fun loadExcludedIngredients(){
        viewModelScope.launch {
            try {
                repo.getExcludedIngredients()
            } catch(e: Exception){
                Log.e("RecipesViewModel", "Error loading excluded ingredients: $e")
            }
        }
    }*/
}