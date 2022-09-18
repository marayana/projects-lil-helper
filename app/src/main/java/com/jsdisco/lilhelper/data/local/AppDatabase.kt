package com.jsdisco.lilhelper.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jsdisco.lilhelper.data.local.models.*
import com.jsdisco.lilhelper.data.local.models.relations.RecipeIngredientCrossRef

/**
 * saves:
 *
 * - Note (id, title, content)
 * - ChecklistItem (id, content, isChecked, listId, listTitle)
 * - Recipe (id, title, instructions, imgUri, categories)
 * - Ingredient (id, name, amount, unit)
 * - RecipeIngredientCrossRef: used to retrieve "RecipeWithIngredients" (Recipe with list of Ingredient)
 * - SettingsIngredient (id, name, isIncluded [in checkLists created from recipes])
 *
 * */

@Database(
    entities = [
        Note::class,
        Drawing::class,
        Recipe::class,
        Ingredient::class,
        RecipeIngredientCrossRef::class,
        SettingsIngredient::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val appDatabaseDao: AppDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabaseInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    ).build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}