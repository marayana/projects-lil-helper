package com.jsdisco.lilhelper.ui.checklists

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.jsdisco.lilhelper.R
import com.jsdisco.lilhelper.adapter.ChecklistsAdapter
import com.jsdisco.lilhelper.data.local.models.ChecklistItem
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
        val adapter = ChecklistsAdapter(deleteItems, toggleCheckbox)
        rvChecklists.adapter = adapter

        binding.fabAddCheckList.setOnClickListener {
            findNavController().navigate(ChecklistsFragmentDirections.actionFragmentChecklistsToFragmentAddChecklist())
        }

        viewModel.checklistItems.observe(viewLifecycleOwner) {
            viewModel.buildChecklists()
            if (viewModel.checklists.value != null) {
                adapter.submitList(viewModel.checklists.value!!)
            }
        }
    }

    private fun showDialog(listId: UUID){

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_dialog_title_checklists))
            .setMultiChoiceItems(arrayOf(getString(R.string.delete_dialog_dont_ask_again)), booleanArrayOf(false)){_, _, _ ->
                viewModel.toggleSettings()
            }
            .setPositiveButton(getString(R.string.delete_dialog_positive_button)) {_, _ ->
                viewModel.deleteChecklistItems(listId)
            }
            .setNegativeButton(getString(R.string.delete_dialog_negative_button)) {_, _ -> }
            .create()
        dialog.show()
    }

    private val deleteItems = { listId: UUID ->
        if (viewModel.askAgainDeleteList.value == true){
            showDialog(listId)
        } else {
            viewModel.deleteChecklistItems(listId)
        }
    }
    private val toggleCheckbox = {item: ChecklistItem -> viewModel.toggleCheckbox(item) }
}