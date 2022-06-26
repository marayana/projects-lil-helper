package com.jsdisco.lilhelper.ui.checklists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jsdisco.lilhelper.databinding.FragmentAddChecklistBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AddChecklistFragment : Fragment() {

    private val viewModel: ChecklistsViewModel by activityViewModels()
    private lateinit var binding:FragmentAddChecklistBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddChecklistBinding.inflate(inflater, container, false)
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
            lifecycleScope.launch {
                withContext(Dispatchers.IO){
                    val newTitle = binding.etAddchecklistTitle.text.toString()

                    viewModel.insertChecklistItems(newTitle)
                    withContext(Dispatchers.Main){
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }
}