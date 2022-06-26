package com.jsdisco.lilhelper.ui.notes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jsdisco.lilhelper.adapter.NotesAdapter
import com.jsdisco.lilhelper.data.models.Note
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

        viewModel.notes.observe(
            viewLifecycleOwner,
            Observer {
                adapter.submitList(it)
                //binding.rvNotes.adapter = NotesAdapter(it, editNote, deleteNote)
            }
        )

        binding.fabAddNote.setOnClickListener {
            findNavController().navigate(NotesFragmentDirections.actionFragmentNotesToFragmentAddNote())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        //binding = null
    }

    private val editNote = { note: Note -> findNavController().navigate(NotesFragmentDirections.actionFragmentNotesToFragmentEditNote(note.id))}
    private val deleteNote = { note: Note -> viewModel.deleteNote(note) }
}