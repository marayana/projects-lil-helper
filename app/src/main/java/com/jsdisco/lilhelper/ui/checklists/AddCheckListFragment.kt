package com.jsdisco.lilhelper.ui.checklists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.jsdisco.lilhelper.data.models.CheckList
import com.jsdisco.lilhelper.data.models.CheckListItem
import com.jsdisco.lilhelper.databinding.FragmentAddCheckListBinding

class AddCheckListFragment : Fragment() {

    private val viewModel: CheckListsViewModel by activityViewModels()
    private lateinit var binding: FragmentAddCheckListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCheckListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ibAddChecklistitem.setOnClickListener {
            val tv = TextView(requireContext())
            val itemText = binding.etAddchecklistItem.text.toString()
            tv.text = itemText
            binding.llAddchecklistItems.addView(tv)
            viewModel.addItem(itemText)
            binding.etAddchecklistItem.setText("")
        }

        binding.btnAddchecklistSave.setOnClickListener {
            val newTitle = binding.etAddchecklistTitle.text.toString()

            viewModel.insertCheckList(newTitle)
            findNavController().navigateUp()
        }
    }
}