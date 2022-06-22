package com.jsdisco.lilhelper.ui.notes

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.jsdisco.lilhelper.data.NotesRepository
import com.jsdisco.lilhelper.data.local.AppDatabase
import com.jsdisco.lilhelper.data.local.getDatabase
import com.jsdisco.lilhelper.data.models.Note
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repo = NotesRepository(database)

    val notes = repo.notes

    fun insertNote(note: Note){
        viewModelScope.launch{
            try{
                repo.insertNote(note)
            } catch(e: Exception){
                Log.e("NotesViewModel", "Error inserting new note: $e")
            }
        }
    }

    fun updateNote(note: Note){
        viewModelScope.launch {
            try {
                repo.updateNote(note)
            } catch(e: Exception){
                Log.e("NotesViewModel", "Error updating note with id ${note.id}: $e")
            }
        }
    }

    fun deleteNote(note: Note){
        viewModelScope.launch {
            try{
                repo.deleteNote(note)
            } catch(e: Exception){
                Log.e("NotesViewModel", "Error deleting note with id ${note.id}: $e")
            }
        }
    }

}