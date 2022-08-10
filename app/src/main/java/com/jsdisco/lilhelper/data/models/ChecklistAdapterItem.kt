package com.jsdisco.lilhelper.data.models

import com.jsdisco.lilhelper.data.local.models.ChecklistItem
import java.util.*
import kotlin.collections.ArrayList

/** This class represents a checklist, as used by the ChecklistsAdapter.
 *
 * The database doesn't store whole lists, it only stores ChecklistItems, each of which knows the
 * list_id (and list_title) it belongs to.
 * */
data class ChecklistAdapterItem(
    val list_id: UUID,
    val list_title: String,
    val list_items: ArrayList<ChecklistItem>
)