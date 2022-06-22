package com.jsdisco.lilhelper.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.jsdisco.lilhelper.R
import com.jsdisco.lilhelper.data.models.Recipe
import com.jsdisco.lilhelper.data.models.RecipeRemote
import com.jsdisco.lilhelper.data.models.relations.RecipeWithIngredients

class RecipesAdapter(
    private var dataset: List<RecipeWithIngredients>
) : RecyclerView.Adapter<RecipesAdapter.ItemViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<RecipeWithIngredients>) {
        dataset = list
        notifyDataSetChanged()
    }


    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvRecipeTitle: TextView = view.findViewById(R.id.tv_item_recipe_title)
        val cvRecipe: CardView = view.findViewById(R.id.cv_item_recipe)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_recipe, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val recipe = dataset[position]

        holder.tvRecipeTitle.text = recipe.recipe.r_title

        holder.cvRecipe.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("recipeIndex", position)
            bundle.putString("recipeTitle", recipe.recipe.r_title)
            holder.itemView.findNavController().navigate(R.id.action_fragmentRecipes_to_fragmentRecipeDetails, bundle)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}