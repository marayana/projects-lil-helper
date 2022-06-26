package com.jsdisco.lilhelper.ui.checklists

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jsdisco.lilhelper.data.AppRepository
import com.jsdisco.lilhelper.data.models.ChecklistAdapterItem
import com.jsdisco.lilhelper.data.models.ChecklistItem
import kotlinx.coroutines.launch
import java.util.*

class ChecklistsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = AppRepository.getRepoInstance(application)

    val checklistItems = repo.checklistItems

    private val newItems = mutableListOf<String>()

    val checklists = MutableLiveData<List<ChecklistAdapterItem>>()
    private val itemListsTemp = mutableListOf<ChecklistAdapterItem>()

    fun buildChecklists() {

        itemListsTemp.clear()

        if (checklistItems.value != null) {

            for (checklistItem in checklistItems.value!!) {

                var addedToItems = false

                for (item in itemListsTemp) {
                    if (checklistItem.cl_id == item.list_id) {
                        item.list_items.add(checklistItem)
                        addedToItems = true
                    }
                }

                if (!addedToItems) {
                    val id = checklistItem.cl_id
                    val newChecklist = ChecklistAdapterItem(
                        id, checklistItem.cl_title, arrayListOf(checklistItem)
                    )
                    itemListsTemp.add(newChecklist)
                }
            }
            checklists.value = itemListsTemp
        }
    }

    fun insertChecklistItems(listTitle: String) {

        viewModelScope.launch {
            val listId = UUID.randomUUID()
            val itemsToInsert = mutableListOf<ChecklistItem>()
            for (content in newItems) {
                val newItem = ChecklistItem(
                    cli_content = content,
                    cli_isChecked = false,
                    cl_id = listId,
                    cl_title = if (listTitle != "") listTitle else "List Title"
                )
                itemsToInsert.add(newItem)
            }
            repo.insertChecklistItems(itemsToInsert)
            newItems.clear()
        }
    }

    fun insertChecklistItemsFromRecipe(listTitle: String, items: List<String>){
        viewModelScope.launch {
            val listId = UUID.randomUUID()
            val itemsToInsert = mutableListOf<ChecklistItem>()
            for (content in items) {
                val newItem = ChecklistItem(
                    cli_content = content,
                    cli_isChecked = false,
                    cl_id = listId,
                    cl_title = listTitle
                )
                itemsToInsert.add(newItem)
            }
            repo.insertChecklistItems(itemsToInsert)
        }
    }

    fun addItem(content: String) {
        if (content != ""){
            newItems.add(content)
        }
    }

    fun deleteChecklistItems(listId: UUID, index: Int){
        viewModelScope.launch{
            repo.deleteChecklistItems(listId)
        }
    }

    fun toggleCheckbox(item: ChecklistItem){
        viewModelScope.launch {
            repo.updateChecklistItem(item)
        }
    }
}