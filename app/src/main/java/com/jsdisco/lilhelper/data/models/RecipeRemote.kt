package com.jsdisco.lilhelper.data.models

data class RecipeRemote(
    val _id: String,
    val title: String,
    val recipeCategory: List<String>,
    val ingredients: List<IngredientRemote>,
    val instructions: String
)