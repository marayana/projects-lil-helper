package com.jsdisco.lilhelper.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(
    @PrimaryKey(autoGenerate = false)
    val r_id: String,
    val r_title: String,
    val r_instructions: String,
    val r_img: String,
    val r_cat_warm: Boolean,
    val r_cat_cold: Boolean,
    val r_cat_salad: Boolean,
    val r_cat_soup: Boolean,
    val r_cat_base: Boolean,
    val r_cat_sweet: Boolean
)