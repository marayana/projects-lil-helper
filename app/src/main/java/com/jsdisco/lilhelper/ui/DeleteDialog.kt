package com.jsdisco.lilhelper.ui


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import com.jsdisco.lilhelper.data.models.Note

/*
class DeleteDialog(context: Context){

    private fun showDialog(context: Context, note: Note){

        val dialog = AlertDialog.Builder(context)
            .setTitle("Diese Notiz endgültig löschen?")
            .setMultiChoiceItems(arrayOf("Nicht erneut fragen"), booleanArrayOf(false)){_, which, isChecked ->
                if (isChecked){
                    Log.e("NotesFragment dialog", "$which: $isChecked")
                } else {
                    Log.e("NotesFragment dialog", "$which: $isChecked")
                }
            }
            .setPositiveButton("Ok") {_, _ ->
                Log.e("NotesFragment dialog", "ok clicked")
                viewModel.deleteNote(note)
            }
            .setNegativeButton("Cancel") {_, _ ->
                Log.e("NotesFragment dialog", "cancel clicked")
            }
            .create()
        dialog.show()

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Test Dialog")
                .setPositiveButton("Ok") { dialog, id ->
                    Log.e("Dialog", "clicked OK")
                }
                .setNegativeButton("Cancel") {dialog, id ->
                    Log.e("Dialog", "clicked cancel")
                }
            builder.create()
        } ?: throw IllegalStateException("Error in Dialog: Activity cannot be null")
    }
}
*/