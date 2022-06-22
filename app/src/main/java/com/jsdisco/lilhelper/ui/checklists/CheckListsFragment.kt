package com.jsdisco.lilhelper.ui.checklists

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.jsdisco.lilhelper.adapter.CheckListsAdapter
import com.jsdisco.lilhelper.data.models.CheckList
import com.jsdisco.lilhelper.data.models.relations.CheckListWithItems
import com.jsdisco.lilhelper.databinding.FragmentCheckListsBinding
import java.lang.Long.getLong

class CheckListsFragment : Fragment() {

    private lateinit var binding: FragmentCheckListsBinding
    private val viewModel: CheckListsViewModel by activityViewModels()

    private lateinit var adapter: CheckListsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvCheckLists = binding.rvCheckLists

        adapter = if (viewModel.checkLists.value != null){
            CheckListsAdapter(viewModel.checkLists.value!!, requireContext(), editCheckList, deleteCheckList)
        } else {
            CheckListsAdapter(emptyList(), requireContext(), editCheckList, deleteCheckList)
        }
        rvCheckLists.adapter = adapter


        viewModel.checkLists.observe(
            viewLifecycleOwner,
            Observer {
                adapter.submitList(it)
            }
        )

        binding.fabAddCheckList.setOnClickListener {
            findNavController().navigate(CheckListsFragmentDirections.actionFragmentCheckListsToFragmentAddCheckList())
        }
    }

    private val editCheckList = {checkList: CheckListWithItems -> viewModel.updateCheckList(checkList)}
    private val deleteCheckList = {checkList: CheckListWithItems -> viewModel.deleteCheckList(checkList)}
}

/*viewModel.checkLists.observe(
            viewLifecycleOwner,
            Observer{
                Log.e("FM checkList observer", "status: ${viewModel.status.value}")
                when(viewModel.status.value){
                    Status.LOADED -> adapter.submitList(it)
                    //Status.INSERTED -> adapter.notifyItemInserted(viewModel.currIndex)
                    Status.DELETED -> {
                        adapter.notifyItemRemoved(viewModel.currIndex)
                        viewModel.resetStatus()
                    }
                    else -> {}
                }
            }
        )*/

/*viewModel.status.observe(
           viewLifecycleOwner,
           Observer {
               when (it){
                   Status.LOADED -> {
                       if (viewModel.checkLists.value != null){
                           adapter.submitList(viewModel.checkLists.value!!)
                           viewModel.resetStatus()
                       }
                   }
                   Status.CHANGED -> {
                       if (viewModel.checkLists.value != null){
                           adapter.notifyItemChanged(viewModel.currIndex)
                           viewModel.resetStatus()
                       }
                   }
                   Status.INSERTED -> {
                       if (viewModel.checkLists.value != null){
                           adapter.notifyItemInserted(viewModel.currIndex)
                           viewModel.resetStatus()
                       }
                   }
                   Status.DELETED -> {
                       if (viewModel.checkLists.value != null){
                           Log.e("FM status observer 1", viewModel.checkLists.value?.size.toString())
                           binding.rvCheckLists.adapter?.notifyItemRemoved(viewModel.currIndex)
                           Log.e("FM status observer 2", viewModel.currIndex.toString())
                           Log.e("FM status observer 3", viewModel.checkLists.value?.size.toString())
                           viewModel.resetStatus()
                       }
                   }
                   else -> {}
               }
           }
       )*/