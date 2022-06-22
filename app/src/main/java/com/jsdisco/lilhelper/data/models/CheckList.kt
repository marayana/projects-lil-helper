package com.jsdisco.lilhelper.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class CheckList(
    @PrimaryKey(autoGenerate = false)
    val cl_id: UUID,
    val cl_title: String
)