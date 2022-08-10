package com.jsdisco.lilhelper.data.local.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity
data class ChecklistItem(
    @PrimaryKey(autoGenerate = true)
    val cli_id: Long = 0,
    val cli_content: String,
    var cli_isChecked: Boolean,
    val cl_id: UUID,
    val cl_title: String
) : Parcelable