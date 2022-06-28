package com.jsdisco.lilhelper.data

import android.content.Context
import android.content.SharedPreferences

const val LIL_HELPER_PREFS = "lilHelperPrefs"

class Prefs(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(LIL_HELPER_PREFS, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    fun setPref(key: String, value: Boolean){
        editor.apply{
            putBoolean(key, value)
            apply()
        }
    }

    fun getPref(key: String): Boolean {
        return prefs.getBoolean(key, false)
    }
}