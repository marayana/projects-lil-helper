package com.jsdisco.lilhelper.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SettingsIngredient(
    @PrimaryKey(autoGenerate = true)
    val si_id: Long = 0,
    val si_name: String,
    var si_included: Boolean
)