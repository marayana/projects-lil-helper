package com.jsdisco.lilhelper.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jsdisco.lilhelper.data.models.*
import com.jsdisco.lilhelper.data.models.relations.CheckListWithItems
import com.jsdisco.lilhelper.data.models.relations.RecipeIngredientCrossRef
import com.jsdisco.lilhelper.data.models.relations.RecipeWithIngredients
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
    suspend fun getRecipeWithIngredients(id: String) : List<RecipeWithIngredients>

    @Query("SELECT * FROM Recipe")
    fun getRecipes() : List<Recipe>

    @Transaction
    @Query("SELECT * FROM Recipe WHERE r_id = :id")
    suspend fun getRecipeById(id: String) : RecipeWithIngredients

    @Query("DELETE FROM Recipe")
    fun deleteRecipes()

    @Query("DELETE FROM Ingredient")
    fun deleteIngredients()

    @Query("DELETE FROM RecipeIngredientCrossRef")
    fun deleteRecipeIngredientCrossRef()


    // CHECKLISTS TABLES
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckList(checkList: CheckList)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckListItem(checkListItem: CheckListItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCheckListItems(items: List<CheckListItem>)

    @Transaction
    @Query("SELECT * FROM CheckList WHERE cl_id = :id")
    suspend fun getCheckListWithItems(id: UUID) : List<CheckListWithItems>

    @Transaction
    @Query("SELECT * FROM CheckList WHERE cl_id = :id")
    suspend fun getCheckListById(id: UUID) : CheckListWithItems

    //@Update
    //suspend fun updateCheckList(checkList: CheckList)

    //@Update
    //suspend fun updateCheckListById(id: UUID)

    @Query("SELECT * FROM CheckList")
    fun getCheckLists() : List<CheckList>

    @Query("DELETE FROM CheckList WHERE cl_id = :id")
    fun deleteCheckListById(id: UUID)

    @Query("DELETE FROM CheckListItem WHERE cli_id = :id")
    fun deleteCheckListItemById(id: Long)

    @Query("DELETE FROM CheckListItem WHERE cl_id = :id")
    fun deleteCheckListItemsByCheckListId(id: UUID)


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
}