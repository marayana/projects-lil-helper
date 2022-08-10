package com.jsdisco.lilhelper.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.jsdisco.lilhelper.data.local.models.Note
import com.jsdisco.lilhelper.databinding.FragmentAddNoteBinding

class AddNoteFragment : Fragment() {

    private val viewModel: NotesViewModel by activityViewModels()
    private lateinit var binding:FragmentAddNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddnoteSave.setOnClickListener {
            val newTitle = binding.etAddnoteTitle.text.toString()
            val newContent = binding.etAddnoteContent.text.toString()
            val newNote = Note(title = newTitle, content = newContent)
            viewModel.insertNote(newNote)
            findNavController().navigateUp()
        }
    }
}