package com.jsdisco.lilhelper.ui.notes

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.jsdisco.lilhelper.R
import com.jsdisco.lilhelper.adapter.NotesAdapter
import com.jsdisco.lilhelper.data.local.models.Note
import com.jsdisco.lilhelper.databinding.FragmentNotesBinding


class NotesFragment : Fragment() {

    private lateinit var binding: FragmentNotesBinding
    private val viewModel: NotesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvNotes = binding.rvNotes
        val adapter = NotesAdapter(emptyList(), editNote, deleteNote)
        rvNotes.adapter = adapter

        viewModel.notes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.fabAddNote.setOnClickListener {
            findNavController().navigate(NotesFragmentDirections.actionFragmentNotesToFragmentAddNote())
        }
    }

    private fun showDialog(note: Note){
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_dialog_title_notes))
            .setMultiChoiceItems(arrayOf(getString(R.string.delete_dialog_dont_ask_again)), booleanArrayOf(false)){_, _, _ ->
                viewModel.toggleSettings()
            }
            .setPositiveButton(getString(R.string.delete_dialog_positive_button)) {_, _ ->
                viewModel.deleteNote(note)
            }
            .setNegativeButton(getString(R.string.delete_dialog_negative_button)) {_, _ -> }
            .create()
        dialog.show()
    }

    private val editNote = { note: Note -> findNavController().navigate(NotesFragmentDirections.actionFragmentNotesToFragmentEditNote(note.id))}
    private val deleteNote = { note: Note ->
        if (viewModel.askAgainDeleteNote.value == true){
            showDialog(note)
        } else {
            viewModel.deleteNote(note)
        }
    }
}