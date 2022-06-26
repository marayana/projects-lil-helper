package com.jsdisco.lilhelper.ui.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.jsdisco.lilhelper.R
import com.jsdisco.lilhelper.adapter.RecipesAdapter
import com.jsdisco.lilhelper.databinding.FragmentRecipesBinding

const val TAG = "RecipesFragment"

class RecipesFragment : Fragment() {

    private val viewModel: RecipesViewModel by activityViewModels()
    private lateinit var binding: FragmentRecipesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvRecipes = binding.rvRecipes
        val adapter = RecipesAdapter(emptyList())
        rvRecipes.adapter = adapter

        binding.ibRecipesDownload.setOnClickListener {
            viewModel.downloadRecipesFromApi()
        }

        binding.cgCategories.setOnCheckedStateChangeListener { _, checkedIds ->

            val checked = if (checkedIds.isEmpty()){
                binding.chipCatAll.isChecked = true
                "all"
            } else {
                when(checkedIds[0]){
                    binding.chipCatWarm.id -> "warm"
                    binding.chipCatCold.id -> "cold"
                    binding.chipCatSalad.id -> "salad"
                    binding.chipCatSoup.id -> "soup"
                    binding.chipCatBase.id -> "base"
                    else -> "all"
                }
            }
            val filtered = viewModel.filterRecipes(checked)
            if (filtered != null){
                adapter.submitList(filtered)
            }
        }

        viewModel.recipes.observe(
            viewLifecycleOwner,
            Observer {
                adapter.submitList(it)
            }
        )

        viewModel.loading.observe(
            viewLifecycleOwner,
            Observer {
                when(it){
                    ApiStatus.LOADING -> binding.pbRecipes.visibility = View.VISIBLE
                    ApiStatus.ERROR -> {
                        binding.pbRecipes.visibility = View.GONE
                        binding.ivRecipesCloudOff.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.pbRecipes.visibility = View.GONE
                        binding.ivRecipesCloudOff.visibility = View.GONE
                    }
                }
            }
        )
    }
}