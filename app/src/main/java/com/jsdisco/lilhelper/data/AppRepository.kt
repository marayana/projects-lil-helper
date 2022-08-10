package com.jsdisco.lilhelper.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jsdisco.lilhelper.data.local.AppDatabase
import com.jsdisco.lilhelper.data.local.models.*
import com.jsdisco.lilhelper.data.local.models.relations.RecipeIngredientCrossRef
import com.jsdisco.lilhelper.data.local.models.relations.RecipeWithIngredients
import com.jsdisco.lilhelper.data.remote.RecipeApi
import com.jsdisco.lilhelper.data.remote.models.RecipeRemote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

const val TAG = "AppRepository"

/**
 *
 * Keeps all data used by the app:
 *
 * - Notes: from roomDB
 * - Checklists: from roomDB
 * - Recipes: from roomDB (or, if no recipes are stored, from the API)
 * - Settings: from sharedPreferences
 *
 * */

class AppRepository(private val database: AppDatabase) {

    private val api = RecipeApi

    /** NOTES */
    val notes: LiveData<List<Note>> = database.appDatabaseDao.getNotes()

    /** CHECK LISTS */
    val checklistItems: LiveData<List<ChecklistItem>> = database.appDatabaseDao.getChecklistItems()

    /** RECIPES */
    private val _recipes = MutableLiveData<List<RecipeWithIngredients>>()
    val recipes: LiveData<List<RecipeWithIngredients>> = _recipes

    private val _currRecipe = MutableLiveData<RecipeWithIngredients>()
    val currRecipe: LiveData<RecipeWithIngredients> = _currRecipe

    /** SETTINGS */
    private val _settingsIngs = MutableLiveData<List<SettingsIngredient>>()
    val settingsIngs: LiveData<List<SettingsIngredient>> = _settingsIngs

    val settingsAskDeleteNote = MutableLiveData(true)
    val settingsAskDeleteList = MutableLiveData(true)
    val settingsLoadImgs = MutableLiveData(false)

    private lateinit var prefs: Prefs


    /** NOTES - PUBLIC */

    suspend fun insertNote(note: Note){
        try {
            database.appDatabaseDao.insertNote(note)
        } catch(e: Exception){
            Log.e(TAG, "Error inserting note into database: $e")
        }
    }

    suspend fun updateNote(note: Note){
        try {
            database.appDatabaseDao.updateNote(note)
        } catch(e: Exception){
            Log.e(TAG, "Error updating note with id ${note.id}: $e")
        }
    }

    suspend fun deleteNote(note: Note){
        try {
            database.appDatabaseDao.deleteNoteById(note.id)
        } catch(e: Exception){
            Log.e(TAG, "Error deleting note with id ${note.id}: $e")
        }
    }


    /** LISTS - PUBLIC */

    suspend fun insertChecklistItems(items: List<ChecklistItem>){
        try {
            database.appDatabaseDao.insertManyChecklistItems(items)
        } catch(e: Exception){
            Log.e(TAG, "Error inserting checklistItems into database: $e")
        }
    }

    suspend fun updateChecklistItem(item: ChecklistItem){
        try{
            database.appDatabaseDao.updateChecklistItem(item)
        } catch(e: Exception){
            Log.e(TAG, "Error updating checklistItem in database: $e")
        }
    }

    suspend fun deleteChecklistItems(listId: UUID){
        try{
            database.appDatabaseDao.deleteChecklistItems(listId)
        } catch(e: Exception){
            Log.e(TAG, "Error deleting checklistItems from database: $e")
        }
    }


    /** RECIPES AND INGREDIENTS */

    private suspend fun getRecipesFromDb(){
        try {
            val recipes = database.appDatabaseDao.getRecipes()
            val recipesWithIngs = recipes.map{database.appDatabaseDao.getRecipeWithIngredients(it.r_id)}
            _recipes.postValue(recipesWithIngs)
        } catch(e: Exception){
            Log.e(TAG, "Error getting recipes from db: $e")
        }
    }

    private suspend fun insertRecipeIntoDb(recipeFromApi: RecipeRemote){
        try {
            val newRecipe = Recipe(
                recipeFromApi._id,
                recipeFromApi.title,
                recipeFromApi.instructions,
                recipeFromApi.img,
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
            Log.e(TAG, "Error inserting recipe into db: $e")
        }
    }

    private fun deleteRecipeTables(){
        try {
            database.appDatabaseDao.deleteRecipes()
            database.appDatabaseDao.deleteIngredients()
            database.appDatabaseDao.deleteRecipeIngredientCrossRef()
        } catch(e: Exception){
            Log.e(TAG, "Error deleting recipe tables: $e")
        }
    }

    private suspend fun initSettingsIngredients(){
        try {
            val hasNoSettingsIngs = database.appDatabaseDao.getSettingsIngsCount() == 0
            if (hasNoSettingsIngs){
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
            Log.e(TAG, "Error getting settingsIngredients: $e")
        }
    }

    /** RECIPES AND INGREDIENTS - PUBLIC */

    suspend fun initRecipeDB(){
        withContext(Dispatchers.IO){
            if (database.appDatabaseDao.getRecipeCount() == 0){
                getRecipesFromApi()
            } else {
                getRecipesFromDb()
            }
            initSettingsIngredients()
        }
    }

    suspend fun getRecipesFromApi(){
        withContext(Dispatchers.IO){
            val recipesFromApi = api.retrofitService.getRecipes().sortedBy{it.title}
            deleteRecipeTables()
            recipesFromApi.forEach{ insertRecipeIntoDb(it) }
            getRecipesFromDb()
        }
    }

    suspend fun getRecipeWithIngredientsById(id: String){
        try {
            _currRecipe.postValue(database.appDatabaseDao.getRecipeWithIngredients(id))
        } catch(e: Exception){
            Log.e(TAG, "Error in getRecipeWithIngredientsById(): $e")
        }
    }


    /** SETTINGS - PUBLIC */

    fun initSettings(context: Context){
        prefs = Prefs(context)
        settingsAskDeleteNote.value = prefs.getPref("prefDeleteNote")
        settingsAskDeleteList.value = prefs.getPref("prefDeleteList")
        settingsLoadImgs.value = prefs.getPref("prefLoadImgs")
    }

    fun toggleSetting(key: String){
        if (key == "prefDeleteNote"){
            settingsAskDeleteNote.value?.let{settingsAskDeleteNote.value = !it}
            prefs.setPref(key, settingsAskDeleteNote.value ?: true)
        }
        if (key == "prefDeleteList"){
            settingsAskDeleteList.value?.let{settingsAskDeleteList.value = !it}
            prefs.setPref(key, settingsAskDeleteList.value ?: true)
        }
        if (key == "prefLoadImgs"){
            settingsLoadImgs.value?.let{settingsLoadImgs.value = !it}
            prefs.setPref(key, settingsLoadImgs.value ?: false)
        }
    }

    suspend fun toggleSettingsIngCheckbox(settingsIng: SettingsIngredient){
        try {
            settingsIng.si_included = !settingsIng.si_included
            database.appDatabaseDao.updateSettingsIng(settingsIng)
        } catch(e: Exception){
            Log.e(TAG, "Error in toggleSettingsIngCheckbox(): $e")
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