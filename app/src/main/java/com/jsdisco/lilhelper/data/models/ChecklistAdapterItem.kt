package com.jsdisco.lilhelper.data.models

import java.util.*
import kotlin.collections.ArrayList

data class ChecklistAdapterItem(
    val list_id: UUID,
    val list_title: String,
    val list_items: ArrayList<ChecklistItem>
)