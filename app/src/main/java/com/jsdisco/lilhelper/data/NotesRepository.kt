package com.jsdisco.lilhelper.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.jsdisco.lilhelper.data.local.AppDatabase
import com.jsdisco.lilhelper.data.models.Note

class NotesRepository(private val database: AppDatabase) {

    val notes: LiveData<List<Note>> = database.appDatabaseDao.getNotes()

    suspend fun insertNote(note: Note){
        try {
            database.appDatabaseDao.insertNote(note)
        } catch(e: Exception){
            Log.e("NotesRepository", "Error inserting note into database: $e")
        }
    }

    suspend fun updateNote(note: Note){
        try {
            database.appDatabaseDao.updateNote(note)
        } catch(e: Exception){
            Log.e("NotesRepository", "Error updating note with id ${note.id}: $e")
        }
    }

    suspend fun deleteNote(note: Note){
        try {
            database.appDatabaseDao.deleteNoteById(note.id)
        } catch(e: Exception){
            Log.e("NotesRepository", "Error deleting note with id ${note.id}: $e")
        }
    }
}