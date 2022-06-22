package com.jsdisco.lilhelper.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class CheckListItem(
    @PrimaryKey(autoGenerate = true)
    val cli_id: Long = 0,
    val cli_content: String,
    val cli_isChecked: Boolean,
    val cl_id: UUID
)