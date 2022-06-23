package com.jsdisco.lilhelper.data.models

data class Settings(
    val askAgainDeleteNotes: Boolean,
    val ingredients: List<SettingsIngredient>
)