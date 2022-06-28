package com.jsdisco.lilhelper.ui.checklists

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.jsdisco.lilhelper.adapter.ChecklistsAdapter
import com.jsdisco.lilhelper.data.models.ChecklistAdapterItem
import com.jsdisco.lilhelper.data.models.ChecklistItem
import com.jsdisco.lilhelper.data.models.Note
import com.jsdisco.lilhelper.databinding.FragmentChecklistsBinding
import java.util.*

class ChecklistsFragment : Fragment() {

    private val viewModel: ChecklistsViewModel by activityViewModels()
    private lateinit var binding: FragmentChecklistsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChecklistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvChecklists = binding.rvCheckLists
        val adapter = ChecklistsAdapter(requireContext(), deleteItems, toggleCheckbox)
        rvChecklists.adapter = adapter

        binding.fabAddCheckList.setOnClickListener {
            findNavController().navigate(ChecklistsFragmentDirections.actionFragmentChecklistsToFragmentAddChecklist())
        }

        viewModel.checklistItems.observe(
            viewLifecycleOwner,
            Observer {
                viewModel.buildChecklists()
                if (viewModel.checklists.value != null){
                    adapter.submitList(viewModel.checklists.value!!)
                }
            }
        )
    }

    private fun showDialog(listId: UUID, index: Int){

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Diese Liste endgültig löschen?")
            .setMultiChoiceItems(arrayOf("Nicht erneut fragen"), booleanArrayOf(false)){_, _, _ ->
                viewModel.toggleSettings()
            }
            .setPositiveButton("Ok") {_, _ ->
                viewModel.deleteChecklistItems(listId, index)
            }
            .setNegativeButton("Cancel") {_, _ -> }
            .create()
        dialog.show()
    }

    private val deleteItems = { listId: UUID, index: Int ->
        if (viewModel.askAgainDeleteList.value == true){
            showDialog(listId, index)
        } else {
            viewModel.deleteChecklistItems(listId, index)
        }
    }
    private val toggleCheckbox = {item: ChecklistItem -> viewModel.toggleCheckbox(item) }
}