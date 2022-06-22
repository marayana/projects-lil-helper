package com.jsdisco.lilhelper.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jsdisco.lilhelper.data.models.*
import com.jsdisco.lilhelper.data.models.relations.RecipeIngredientCrossRef

@Database(
    entities = [
        Note::class,
        CheckList::class,
        CheckListItem::class,
        Recipe::class,
        Ingredient::class,
        RecipeIngredientCrossRef::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val appDatabaseDao: AppDatabaseDao

    /*companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_db"
                ).build().also { INSTANCE = it }
            }
        }
    }*/
}



private lateinit var INSTANCE: AppDatabase

fun getDatabase(context: Context): AppDatabase {
    synchronized(AppDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            ).build()
        }
    }
    return INSTANCE
}

