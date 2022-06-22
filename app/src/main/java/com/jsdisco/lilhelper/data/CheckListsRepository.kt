package com.jsdisco.lilhelper.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jsdisco.lilhelper.data.local.AppDatabase
import com.jsdisco.lilhelper.data.models.CheckList
import com.jsdisco.lilhelper.data.models.CheckListItem
import com.jsdisco.lilhelper.data.models.relations.CheckListWithItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class CheckListsRepository(private val database: AppDatabase) {

    private val _checkLists = MutableLiveData<List<CheckListWithItems>>()
    val checkLists: LiveData<List<CheckListWithItems>>
        get() = _checkLists


    suspend fun loadCheckLists(){
        withContext(Dispatchers.IO){
            val lists = database.appDatabaseDao.getCheckLists()
            val listsWithItems = lists.map{database.appDatabaseDao.getCheckListWithItems(it.cl_id)[0]}
            _checkLists.postValue(listsWithItems)
        }
    }

    suspend fun insertCheckList(title: String, items: List<String>) {
        withContext(Dispatchers.IO){
            val id = UUID.randomUUID()
            database.appDatabaseDao.insertCheckList(CheckList(id, title))
            val checkListItems = items.map{CheckListItem(cli_content = it, cli_isChecked = false, cl_id = id)}
            database.appDatabaseDao.insertAllCheckListItems(checkListItems)
            loadCheckLists()
        }
    }

    suspend fun updateCheckList(checkList: CheckListWithItems) {
        withContext(Dispatchers.IO){
            //database.appDatabaseDao.updateCheckListById(checkList.checkList.cl_id)
        }
    }

    suspend fun deleteCheckList(checkList: CheckListWithItems) {
        Log.e("Repo", "delete")
        withContext(Dispatchers.IO){
            database.appDatabaseDao.deleteCheckListItemsByCheckListId(checkList.checkList.cl_id)
            database.appDatabaseDao.deleteCheckListById(checkList.checkList.cl_id)
            loadCheckLists()
        }
    }
}