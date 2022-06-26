package com.jsdisco.lilhelper.ui.checklists

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
                    Log.e("adapter submit", "oh")
                }
            }
        )
/*
        viewModel.checklists.observe(
            viewLifecycleOwner,
            Observer{
                if (it != null){
                    adapter.submitList(it)
                }
                Log.e("checklists observer", it.size.toString())
            }
        )*/
        /*
        viewModel.checklistItems.observe(
            viewLifecycleOwner,
            Observer {
                Log.e("checklistItems observer", it.toString())
                if (viewModel.status.value == Status.INIT){
                    viewModel.initChecklists()
                }
            }
        )
        viewModel.status.observe(
            viewLifecycleOwner,
            Observer {
                Log.e("status observer", it.toString())

                when (it) {
                    Status.INIT -> {
                        if (viewModel.checklists.value != null){
                            Log.e("status observer", "INIT 1")
                            adapter.submitList(viewModel.checklists.value!!)
                            viewModel.resetStatus()
                        } else {
                            Log.e("status observer", "INIT 2")
                        }
                    }
                    Status.INSERTED -> {
                        adapter.submitList(viewModel.checklists.value!!)
                        viewModel.resetStatus()
                    }
                    Status.DELETED -> {
                        if (viewModel.currIndex != null){
                            adapter.notifyItemRemoved(viewModel.currIndex!!)
                            viewModel.resetStatus()
                        }
                    }

                    else -> {}
                }
            }
        )*/


    }

    private val deleteItems = { listId: UUID, index: Int -> viewModel.deleteChecklistItems(listId, index) }
    private val toggleCheckbox = {item: ChecklistItem -> viewModel.toggleCheckbox(item) }
}