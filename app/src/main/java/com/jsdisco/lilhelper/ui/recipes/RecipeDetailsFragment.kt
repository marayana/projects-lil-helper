package com.jsdisco.lilhelper.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.jsdisco.lilhelper.R
import com.jsdisco.lilhelper.data.local.models.Note
import com.jsdisco.lilhelper.data.remote.BASE_URL
import com.jsdisco.lilhelper.data.remote.APITOKEN
import com.jsdisco.lilhelper.databinding.FragmentRecipeDetailsBinding
import com.jsdisco.lilhelper.ui.notes.NotesViewModel


class RecipeDetailsFragment : Fragment() {

    private val viewModel: RecipesViewModel by activityViewModels()
    private val notesViewModel: NotesViewModel by activityViewModels()
    private lateinit var binding: FragmentRecipeDetailsBinding

    private var recipeTitle: String = ""
    private var recipeId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        arguments?.let {
            recipeTitle = it.getString("recipeTitle").toString()
            recipeId = it.getString("recipeId").toString()
        }

        binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRecipeWithIngredientsById(recipeId)
        viewModel.currRecipe.observe(viewLifecycleOwner) { recipe ->
            if (recipe != null) {
                val ingStr =
                    recipe.ingredients.joinToString(separator = "\n") { "${it.i_name} ${it.i_amount} ${it.i_unit} " }
                binding.tvRecipeDetailsIngs.text = ingStr

                binding.tvRecipeDetailsInstr.text = recipe.recipe.r_instructions

                // recipe images
                if (viewModel.loadImgs.value == true){
                    val imgUrl = "${BASE_URL}img/${recipe.recipe.r_img}.jpg"
                    val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
                    binding.ivRecipeDetails.load(imgUri){
                        addHeader("Authorization", "Bearer $APITOKEN")
                        error(R.drawable.defaultimg)
                    }
                } else {
                    binding.ivRecipeDetails.setImageResource(R.drawable.defaultimg)
                }
            }
        }

        // creating a note from recipe ingredients
        binding.btnRecipeDetailsCreateList.setOnClickListener {
            viewModel.createNoteFromRecipe()
            findNavController().navigate(RecipeDetailsFragmentDirections.actionGlobalNavNestedNotes())
        }
    }
}