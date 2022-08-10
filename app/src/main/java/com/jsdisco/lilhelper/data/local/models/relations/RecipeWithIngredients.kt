package com.jsdisco.lilhelper.data.local.models.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.jsdisco.lilhelper.data.local.models.Ingredient
import com.jsdisco.lilhelper.data.local.models.Recipe

data class RecipeWithIngredients(
    @Embedded val recipe: Recipe,
    @Relation(
        parentColumn = "r_id",
        entityColumn = "i_id",
        associateBy = Junction(RecipeIngredientCrossRef::class)
    )
    val ingredients: List<Ingredient>
)