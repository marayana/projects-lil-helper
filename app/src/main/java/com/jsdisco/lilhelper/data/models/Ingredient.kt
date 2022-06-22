package com.jsdisco.lilhelper.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ingredient(
    @PrimaryKey(autoGenerate = false)
    val i_id: String,
    val i_name: String,
    val i_amount: String,
    val i_unit: String,
    val excluded_from_shopping_list: Boolean = false
)