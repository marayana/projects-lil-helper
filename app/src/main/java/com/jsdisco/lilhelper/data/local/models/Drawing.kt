package com.jsdisco.lilhelper.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Drawing(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val path: String
)
