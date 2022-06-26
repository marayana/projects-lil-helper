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
        ChecklistItem::class,
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


/*
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
*/
