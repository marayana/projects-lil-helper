package com.jsdisco.lilhelper.ui.drawings

import android.app.Application
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jsdisco.lilhelper.R
import com.jsdisco.lilhelper.data.AppRepository
import com.jsdisco.lilhelper.data.local.models.Drawing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Colours {
    val black = listOf(32, 32, 32)
    val white = listOf(238, 238, 238)
    val red = listOf(232,26,50)
    val green = listOf(79,152,6)
    val blue = listOf(31,91,195)
    val yellow = listOf(255,183,0)
    val violet = listOf(205,67,255)
    val teal = listOf(31,219,225)
}

class DrawingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = AppRepository.getRepoInstance(application)
    val drawings = repo.drawings

    val askAgainDeleteDrawing = repo.settingsAskDeleteDrawing

    val strokeWidth = 12f



    fun saveDrawing(path: String, title: String){
        val drawing = Drawing(path = path, title = title)
        viewModelScope.launch(Dispatchers.IO){
            repo.insertDrawing(drawing)
        }
    }

    fun deleteDrawing(id: Long){
        viewModelScope.launch(Dispatchers.IO){
            repo.deleteDrawing(id)
        }
    }

    fun toggleSettings(){
        repo.toggleSetting("prefDeleteDrawing")
    }

}