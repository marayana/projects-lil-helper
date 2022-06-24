package com.jsdisco.lilhelper.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jsdisco.lilhelper.data.local.AppDatabase
import com.jsdisco.lilhelper.data.models.*
import com.jsdisco.lilhelper.data.models.relations.RecipeIngredientCrossRef
import com.jsdisco.lilhelper.data.models.relations.RecipeWithIngredients
import com.jsdisco.lilhelper.data.remote.RecipeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class AppRepository(private val database: AppDatabase) {

    private val api = RecipeApi

    private val _recipes = MutableLiveData<List<RecipeWithIngredients>>()
    val recipes: LiveData<List<RecipeWithIngredients>>
        get() = _recipes

    private val _settingsIngs = MutableLiveData<List<SettingsIngredient>>()
    val settingsIngs: LiveData<List<SettingsIngredient>>
        get() = _settingsIngs

    val notes: LiveData<List<Note>> = database.appDatabaseDao.getNotes()


    /** NOTES - PUBLIC */

    suspend fun insertNote(note: Note){
        try {
            database.appDatabaseDao.insertNote(note)
        } catch(e: Exception){
            Log.e("AppRepository", "Error inserting note into database: $e")
        }
    }

    suspend fun updateNote(note: Note){
        try {
            database.appDatabaseDao.updateNote(note)
        } catch(e: Exception){
            Log.e("AppRepository", "Error updating note with id ${note.id}: $e")
        }
    }

    suspend fun deleteNote(note: Note){
        try {
            database.appDatabaseDao.deleteNoteById(note.id)
        } catch(e: Exception){
            Log.e("AppRepository", "Error deleting note with id ${note.id}: $e")
        }
    }


    /** RECIPES AND INGREDIENTS */

    private suspend fun getRecipesFromDb(){
        try {
            val recipes = database.appDatabaseDao.getRecipes()
            val recipesWithIngs = recipes.map{database.appDatabaseDao.getRecipeWithIngredients(it.r_id)[0]}
            _recipes.postValue(recipesWithIngs)
        } catch(e: Exception){
            Log.e("AppRepo", "Error getting recipes from db: $e")
        }
    }

    private suspend fun insertRecipeIntoDb(recipeFromApi: RecipeRemote){
        try {
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
                database.appDatabaseDao.insertIngredient(Ingredient(it._id, it.name, it.amount, it.unit))
                database.appDatabaseDao.insertRecipeIngredientCrossRef(RecipeIngredientCrossRef(recipeFromApi._id, it._id))
            }
        } catch(e: Exception){
            Log.e("AppRepo", "Error inserting recipe into db: $e")
        }
    }

    private fun deleteRecipeTables(){
        try {
            database.appDatabaseDao.deleteRecipes()
            database.appDatabaseDao.deleteIngredients()
            database.appDatabaseDao.deleteRecipeIngredientCrossRef()
        } catch(e: Exception){
            Log.e("AppRepo", "Error deleting recipe tables: $e")
        }
    }

    private suspend fun initSettingsIngredients(){
        try {
            val hasNoIngs = database.appDatabaseDao.getSettingsIngsCount() == 0
            if (hasNoIngs){
                val excluded = listOf("Curry", "Dill", "Gemüsebrühe", "Gewürze", "Kurkuma", "Liebstöckel", "Lorbeer", "Majoran", "Mehl", "Muskat", "Olivenöl", "Oregano", "Paprika (rosenscharf)", "Pfeffer", "Pfeffer (Cayenne)", "Pfeffer (schwarz)", "Rapsöl", "Rauchsalz", "Rohrohrzucker", "Salz", "Senf", "Thymian", "Wasser", "Zucker" )

                val allIngs = database.appDatabaseDao.getIngredients()
                val allIngNames = allIngs.map { it.i_name }.distinct()

                val allSettingsIngs = allIngNames.map{name ->
                    val isIncluded = !excluded.contains(name)
                    SettingsIngredient(si_name = name, si_included = isIncluded)
                }
                database.appDatabaseDao.insertAllSettingsIngs(allSettingsIngs)
            }
            val ings = database.appDatabaseDao.getSettingsIngs()
            _settingsIngs.postValue(ings)

        } catch(e: Exception){
            Log.e("AppRepo", "Error getting settingsIngredients: $e")
        }
    }


    /** RECIPES AND INGREDIENTS - PUBLIC */

    suspend fun initRecipeDB(){
        withContext(Dispatchers.IO){
            try {
                if (database.appDatabaseDao.getRecipeCount() == 0){
                    getRecipesFromApi()
                } else {
                    getRecipesFromDb()
                }
                initSettingsIngredients()
            } catch(e: Exception){
                Log.e("AppRepo", "Error in initRecipeDB: $e")
            }
        }
    }

    suspend fun getRecipesFromApi(){
        try {
            deleteRecipeTables()
            val recipesFromApi = api.retrofitService.getRecipes().sortedBy{it.title}
            recipesFromApi.forEach{ insertRecipeIntoDb(it) }
            getRecipesFromDb()
        } catch(e: Exception){
            Log.e("AppRepo", "Error getting recipes from api: $e")
        }
    }

    suspend fun toggleSettingsIngCheckbox(settingsIng: SettingsIngredient){
        try {
            settingsIng.si_included = !settingsIng.si_included
            database.appDatabaseDao.updateSettingsIng(settingsIng)
        } catch(e: Exception){
            Log.e("AppRepo", "Error in toggleSettingsIngCheckbox: $e")
        }
    }




    companion object {
        private var repo: AppRepository? = null

        fun getRepoInstance(context: Context) : AppRepository = repo ?: buildRepo(
            AppDatabase.getDatabaseInstance(context.applicationContext)
        ).also { repo = it }

        private fun buildRepo(database: AppDatabase) : AppRepository = AppRepository(database)
    }

}