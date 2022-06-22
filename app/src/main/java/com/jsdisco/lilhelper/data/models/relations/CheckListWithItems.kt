package com.jsdisco.lilhelper.data.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.jsdisco.lilhelper.data.models.CheckList
import com.jsdisco.lilhelper.data.models.CheckListItem

data class CheckListWithItems(
    @Embedded val checkList: CheckList,
    @Relation(
        parentColumn = "cl_id",
        entityColumn = "cl_id",
    )
    val checkListItems: List<CheckListItem>
)