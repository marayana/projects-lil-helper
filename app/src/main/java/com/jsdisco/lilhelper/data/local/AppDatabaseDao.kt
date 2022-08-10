package com.jsdisco.lilhelper.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jsdisco.lilhelper.data.local.models.*
import com.jsdisco.lilhelper.data.local.models.relations.RecipeIngredientCrossRef
import com.jsdisco.lilhelper.data.local.models.relations.RecipeWithIngredients
import java.util.*

@Dao
interface AppDatabaseDao {

    // RECIPES TABLES
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRecipes(recipes: List<Recipe>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: Ingredient)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeIngredientCrossRef(crossRef: RecipeIngredientCrossRef)

    @Transaction
    @Query("SELECT * FROM Recipe WHERE r_id = :id")
    suspend fun getRecipeWithIngredients(id: String) : RecipeWithIngredients

    @Query("SELECT * FROM Recipe")
    fun getRecipes() : List<Recipe>

    @Query("SELECT COUNT(*) FROM Recipe")
    fun getRecipeCount() : Int

    @Query("SElECT * FROM Ingredient")
    fun getIngredients() : List<Ingredient>

    @Query("SELECT * FROM Ingredient WHERE i_name = :name")
    fun getIngredientByName(name: String) : Ingredient

    @Transaction
    @Query("SELECT * FROM Recipe WHERE r_id = :id")
    suspend fun getRecipeById(id: String) : RecipeWithIngredients

    @Query("DELETE FROM Recipe")
    fun deleteRecipes()

    @Query("DELETE FROM Ingredient")
    fun deleteIngredients()

    @Query("DELETE FROM RecipeIngredientCrossRef")
    fun deleteRecipeIngredientCrossRef()


    // CHECKLISTITEMS TABLE

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChecklistItem(item: ChecklistItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertManyChecklistItems(items: List<ChecklistItem>)

    @Update
    suspend fun updateChecklistItem(item: ChecklistItem)

    @Query("DELETE FROM ChecklistItem WHERE cl_id = :listId")
    suspend fun deleteChecklistItems(listId: UUID)

    @Query("SELECT * FROM ChecklistItem")
    fun getChecklistItems() : LiveData<List<ChecklistItem>>


    // NOTES TABLE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Query("SELECT * FROM Note")
    fun getNotes() : LiveData<List<Note>>

    @Query("DELETE FROM Note WHERE id = :id")
    suspend fun deleteNoteById(id: Long)

    @Query("DELETE FROM Note")
    suspend fun deleteAllNotes()


    // SETTINGS INGREDIENTS
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettingsIng(settingsIng: SettingsIngredient)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSettingsIngs(ings: List<SettingsIngredient>)

    @Update
    suspend fun updateSettingsIng(settingsIng: SettingsIngredient)

    @Query("SELECT * FROM SettingsIngredient ORDER BY si_name")
    fun getSettingsIngs() : List<SettingsIngredient>

    @Query("SELECT * FROM SettingsIngredient WHERE si_id = :id")
    fun getSettingsIngById(id: Long) : SettingsIngredient

    @Query("SELECT * FROM SettingsIngredient WHERE si_included = 0")
    fun getExcludedIngredients() : List<SettingsIngredient>

    @Query("SELECT COUNT(*) FROM SettingsIngredient")
    fun getSettingsIngsCount() : Int

}