package com.jsdisco.lilhelper.ui.checklists

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jsdisco.lilhelper.data.CheckListsRepository
import com.jsdisco.lilhelper.data.local.getDatabase
import com.jsdisco.lilhelper.data.models.CheckList
import com.jsdisco.lilhelper.data.models.CheckListItem
import com.jsdisco.lilhelper.data.models.relations.CheckListWithItems
import kotlinx.coroutines.launch

enum class Status { LOADED, DELETED, DONE }

class CheckListsViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repo = CheckListsRepository(database)

    val checkLists = repo.checkLists

    private val newItems = mutableListOf<String>()

    init {
        loadCheckLists()
    }

    private fun loadCheckLists() {
        viewModelScope.launch {
            try {
                repo.loadCheckLists()
            } catch (e: Exception) {
                Log.e("CheckListsViewModel", "Error loading checkLists from database: $e")
            }
        }
    }

    fun insertCheckList(title: String) {
        viewModelScope.launch {
            try {
                if (checkLists.value != null){
                    //currIndex = checkLists.value!!.size
                    repo.insertCheckList(title, newItems)
                    newItems.clear()
                }
            } catch (e: Exception) {
                Log.e("CheckListsViewModel", "Error inserting new checkList: $e")
            }
        }
    }
    fun addItem(item: String) {
        newItems.add(item)
    }

    fun deleteCheckList(checkList: CheckListWithItems) {
        viewModelScope.launch {
            try {
                if (checkLists.value != null){
                    //currIndex = checkLists.value!!.indexOf(checkList)
                    repo.deleteCheckList(checkList)
                }
            } catch (e: Exception) {
                Log.e(
                    "CheckListsViewModel",
                    "Error deleting note with id ${checkList.checkList.cl_id}: $e"
                )
            }
        }
    }

    fun createListFromRecipe(title: String, items: List<String>) {
        viewModelScope.launch {
            try {
                repo.insertCheckList(title, items)
            } catch (e: Exception) {
                Log.e("CheckListsViewModel", "Error creating new recipe checkList: $e")
            }
        }
    }



    fun updateCheckList(checkList: CheckListWithItems) {
        viewModelScope.launch {
            try {
                if (checkLists.value != null){
                    //currIndex = checkLists.value!!.indexOf(checkList)
                    repo.updateCheckList(checkList)
                }
            } catch (e: Exception) {
                Log.e(
                    "CheckListsViewModel",
                    "Error updating checkList with id ${checkList.checkList.cl_id}: $e"
                )
            }
        }
    }
}
/*
private val _status = MutableLiveData(Status.LOADED)
val status: LiveData<Status>
    get() = _status

var currIndex = -1*/
/*
 fun resetStatus() {
        _status.value = Status.DONE
        currIndex = -1
    }
 */