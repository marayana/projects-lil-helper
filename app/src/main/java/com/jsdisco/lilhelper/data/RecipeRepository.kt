package com.jsdisco.lilhelper.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jsdisco.lilhelper.data.local.AppDatabase
import com.jsdisco.lilhelper.data.models.Ingredient
import com.jsdisco.lilhelper.data.models.Recipe
import com.jsdisco.lilhelper.data.models.RecipeRemote
import com.jsdisco.lilhelper.data.models.relations.RecipeIngredientCrossRef
import com.jsdisco.lilhelper.data.models.relations.RecipeWithIngredients
import com.jsdisco.lilhelper.data.remote.RecipeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class RecipeRepository(private val api: RecipeApi, private val database: AppDatabase) {

    private val _recipes = MutableLiveData<List<RecipeWithIngredients>>()
    val recipes: LiveData<List<RecipeWithIngredients>>
        get() = _recipes

    suspend fun getRecipesFromApi(){
        withContext(Dispatchers.IO){
            deleteRecipeTables()
            val recipesFromApi = api.retrofitService.getRecipes().sortedBy{it.title}
            recipesFromApi.forEach{ insertRecipeIntoDb(it) }
            getRecipesFromDb()
        }
    }

    private suspend fun insertRecipeIntoDb(recipeFromApi: RecipeRemote){
        withContext(Dispatchers.IO){
            val newRecipe = Recipe(
                recipeFromApi._id,
                recipeFromApi.title,
                recipeFromApi.instructions,
                recipeFromApi.recipeCategory.contains("warm"),
                recipeFromApi.recipeCategory.contains("kalt"),
                recipeFromApi.recipeCategory.contains("salat"),
                recipeFromApi.recipeCategory.contains("suppe"),
                recipeFromApi.recipeCategory.contains("basis"),
                recipeFromApi.recipeCategory.contains("süss")
            )
            database.appDatabaseDao.insertRecipe(newRecipe)
            recipeFromApi.ingredients.forEach{
                database.appDatabaseDao.insertIngredient(Ingredient(it._id, it.name, it.amount, it.unit, false))
                database.appDatabaseDao.insertRecipeIngredientCrossRef(RecipeIngredientCrossRef(recipeFromApi._id, it._id))
            }
        }
    }

    suspend fun getRecipesFromDb(){
        withContext(Dispatchers.IO){
            val recipes = database.appDatabaseDao.getRecipes()
            val recipesWithIngs = recipes.map{database.appDatabaseDao.getRecipeWithIngredients(it.r_id)[0]}
            _recipes.postValue(recipesWithIngs)
        }
    }

    private suspend fun deleteRecipeTables(){
        withContext(Dispatchers.IO){
            database.appDatabaseDao.deleteRecipes()
            database.appDatabaseDao.deleteIngredients()
            database.appDatabaseDao.deleteRecipeIngredientCrossRef()
        }
    }
}