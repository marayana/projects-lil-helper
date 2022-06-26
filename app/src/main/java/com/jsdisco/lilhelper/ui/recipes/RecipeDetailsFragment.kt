package com.jsdisco.lilhelper.ui.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Transformations.map
import androidx.navigation.fragment.findNavController
import com.jsdisco.lilhelper.data.models.Note
import com.jsdisco.lilhelper.databinding.FragmentRecipeDetailsBinding
import com.jsdisco.lilhelper.ui.checklists.ChecklistsViewModel
import com.jsdisco.lilhelper.ui.notes.NotesViewModel

class RecipeDetailsFragment : Fragment() {

    private val viewModel: RecipesViewModel by activityViewModels()
    private val checklistsViewModel: ChecklistsViewModel by activityViewModels()
    private lateinit var binding: FragmentRecipeDetailsBinding

    private var recipeTitle: String = ""
    private var recipeIndex: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        arguments?.let {
            recipeIndex = it.getInt("recipeIndex")
            recipeTitle = it.getString("recipeTitle").toString()
        }

        binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipe = viewModel.recipes.value?.get(recipeIndex)

        if (recipe != null){
            val ingStr = recipe.ingredients.joinToString(separator = "\n") {"${it.i_name} ${it.i_amount} ${it.i_unit} "}
            binding.tvRecipeDetailsIngs.text = ingStr

            binding.tvRecipeDetailsInstr.text = recipe.recipe.r_instructions

            binding.btnRecipeDetailsCreateList.setOnClickListener {
                val excludedIngs = viewModel.settingsIngs.value?.filter { !it.si_included }?.map { it.si_name }

                val ingItems = if (excludedIngs != null){
                    val filtered = recipe.ingredients.filter{!excludedIngs.contains(it.i_name)}
                    filtered.map{"${it.i_name} ${it.i_amount} ${it.i_unit} "}
                } else {
                    recipe.ingredients.map{"${it.i_name} ${it.i_amount} ${it.i_unit} "}
                }

                checklistsViewModel.insertChecklistItemsFromRecipe(recipe.recipe.r_title, ingItems)

                findNavController().navigate(RecipeDetailsFragmentDirections.actionGlobalNavNestedChecklists())
            }
        }
    }
}