package com.jsdisco.lilhelper.ui.notes

import android.app.Application
import androidx.lifecycle.*
import com.jsdisco.lilhelper.data.AppRepository
import com.jsdisco.lilhelper.data.models.Note
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = AppRepository.getRepoInstance(application)

    val notes = repo.notes

    val askAgainDeleteNote = repo.settingsAskDeleteNote

    fun insertNote(note: Note){
        if (note.title == ""){
            note.title = "Note Title"
        }
        viewModelScope.launch{
            repo.insertNote(note)
        }
    }

    fun updateNote(note: Note){
        if (note.title == ""){
            note.title = "Note Title"
        }
        viewModelScope.launch {
            repo.updateNote(note)
        }
    }

    fun deleteNote(note: Note){
        viewModelScope.launch {
            repo.deleteNote(note)
        }
    }

    fun toggleSettings(){
        repo.toggleSetting("prefDeleteNote")
    }

}