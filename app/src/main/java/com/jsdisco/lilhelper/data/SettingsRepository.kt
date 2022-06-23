package com.jsdisco.lilhelper.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jsdisco.lilhelper.data.local.AppDatabase
import com.jsdisco.lilhelper.data.models.Ingredient
import com.jsdisco.lilhelper.data.models.Settings
import com.jsdisco.lilhelper.data.models.SettingsIngredient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext


class SettingsRepository(private val database: AppDatabase) {

    private val _settingsIngs = MutableLiveData<List<SettingsIngredient>>()
    val settingsIngs: LiveData<List<SettingsIngredient>>
        get() = _settingsIngs


    suspend fun initSettings() {
        withContext(Dispatchers.IO) {
            val hasIngs = database.appDatabaseDao.getSettingsIngs().isNotEmpty()
            if (!hasIngs){
                initIngs()
            }
            val ings = database.appDatabaseDao.getSettingsIngs()
            _settingsIngs.postValue(ings)
        }
    }

    private suspend fun initIngs(){
        val allIngNames = database.appDatabaseDao.getIngredients().map { it.i_name }.distinct()
        val allSettingsIngs = allIngNames.map {name -> SettingsIngredient(si_name = name, si_included = true) }
        database.appDatabaseDao.insertAllSettingsIngs(allSettingsIngs)

    }
}

/*
suspend fun saveSetting(key: String, value: String) {
    val dataStoreKey = stringPreferencesKey(key)
    dataStore.edit { settings ->
        settings[dataStoreKey] = value
    }
}

suspend fun readSetting(key: String): String? {
    val dataStoreKey = stringPreferencesKey(key)
    val setting = dataStore.data.first()
    return setting[dataStoreKey]
}
*/
/*suspend fun initSettings() {
        withContext(Dispatchers.IO) {
            /*val ingredients = database.appDatabaseDao.getIngredients()
            val hasSettings = readSetting(ingredients[0].i_name)
            if (hasSettings == null) {
                ingredients.forEach { saveSetting(it.i_name, "false") }
            }*/

            val key = "askAgainDeleteNotes"
            val value = readSetting(key)
            if (value == null){
                saveSetting(key, "false")
            }
            Log.e("SettingsRepo", value.toString())

        }
    }*/