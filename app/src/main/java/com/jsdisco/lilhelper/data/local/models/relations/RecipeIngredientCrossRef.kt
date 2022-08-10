package com.jsdisco.lilhelper.data.local.models.relations

import androidx.room.Entity

@Entity(primaryKeys = ["r_id", "i_id"])
data class RecipeIngredientCrossRef(
    val r_id: String,
    val i_id: String
)