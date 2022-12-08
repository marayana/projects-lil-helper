package com.jsdisco.lilhelper.ui.recipes

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.jsdisco.lilhelper.adapter.RecipesAdapter
import com.jsdisco.lilhelper.databinding.FragmentRecipesBinding

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

        val loadImgs = viewModel.loadImgs.value ?: false

        val rvRecipes = binding.rvRecipes
        val adapter = RecipesAdapter(emptyList(), loadImgs)
        rvRecipes.adapter = adapter

        binding.ibRecipesDownload.setOnClickListener {
            viewModel.downloadRecipesFromApi()
        }


        binding.cgCategories.setOnCheckedStateChangeListener { _, checkedIds ->

            // if no chip is checked, chip "all" is automatically checked
            val checked = if (checkedIds.isEmpty()){
                binding.chipCatAll.isChecked = true
                "all"
            } else {
                // checkedIds is always a list with one item (radio button behaviour)
                when(checkedIds[0]){
                    binding.chipCatWarm.id -> "warm"
                    binding.chipCatCold.id -> "cold"
                    binding.chipCatSalad.id -> "salad"
                    binding.chipCatSoup.id -> "soup"
                    binding.chipCatSweet.id -> "sweet"
                    binding.chipCatBase.id -> "base"
                    else -> "all"
                }
            }
            val filtered = viewModel.filterRecipes(checked)
            if (filtered != null){
                adapter.submitList(filtered)
            }
        }

        viewModel.recipes.observe(viewLifecycleOwner){

            adapter.submitList(it)

            // "misusing" observer to reset chips when leaving/re-entering screen, because this fires once when view is created
            // (resetting outside of observer doesn't work)
            binding.chipCatAll.isChecked = true
            binding.chipCatSalad.isChecked = false
        }

        viewModel.loading.observe(viewLifecycleOwner) {

            when (it) {
                ApiStatus.LOADING -> binding.pbRecipes.visibility = View.VISIBLE
                ApiStatus.ERROR -> {
                    binding.pbRecipes.visibility = View.GONE
                    showDialog()
                    viewModel.resetLoadingStatus()
                }
                else -> {
                    binding.pbRecipes.visibility = View.GONE
                }
            }
        }
    }

    private fun showDialog(){
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage("Beim Download der Rezepte ist ein Fehler aufgetreten, bitte später erneut versuchen.")
            .setPositiveButton("Ok") {_, _ -> }
            .create()
        dialog.show()
    }
}