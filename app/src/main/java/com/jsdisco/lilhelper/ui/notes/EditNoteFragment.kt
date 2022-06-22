package com.jsdisco.lilhelper.ui.notes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.jsdisco.lilhelper.data.models.Note
import com.jsdisco.lilhelper.databinding.FragmentAddNoteBinding
import com.jsdisco.lilhelper.databinding.FragmentEditNoteBinding

class EditNoteFragment : Fragment() {

    private lateinit var binding: FragmentEditNoteBinding
    private val viewModel: NotesViewModel by activityViewModels()

    private var noteId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        arguments?.let{
            noteId = it.getLong("noteId")
        }

        binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val note = viewModel.notes.value?.find{it.id == noteId}

        if (note != null){
            binding.etEditnoteTitle.setText(note.title)
            binding.etEditnoteContent.setText(note.content)
        }

        binding.btnEditnoteSave.setOnClickListener {
            if (note != null){
                note.title = binding.etEditnoteTitle.text.toString()
                note.content = binding.etEditnoteContent.text.toString()
                viewModel.updateNote(note)
            }
            findNavController().navigateUp()
        }
    }

}