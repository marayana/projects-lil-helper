package com.jsdisco.lilhelper.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.jsdisco.lilhelper.R
import com.jsdisco.lilhelper.data.local.models.relations.RecipeWithIngredients
import com.jsdisco.lilhelper.data.remote.APITOKEN
import com.jsdisco.lilhelper.data.remote.BASE_URL
import com.jsdisco.lilhelper.ui.recipes.RecipesFragmentDirections

class RecipesAdapter(
    private var dataset: List<RecipeWithIngredients>,
    private val loadImgs: Boolean
) : RecyclerView.Adapter<RecipesAdapter.ItemViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<RecipeWithIngredients>) {
        dataset = list
        notifyDataSetChanged()
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val cvRecipe: CardView = view.findViewById(R.id.cv_item_recipe)
        val tvRecipeTitle: TextView = view.findViewById(R.id.tv_item_recipe_title)
        val ivRecipe: ImageView = view.findViewById(R.id.iv_item_recipe_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_recipe, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val recipe = dataset[position]

        holder.tvRecipeTitle.text = recipe.recipe.r_title.replace("-", "\u00AD-")

        if (loadImgs){
            val imgUrl = "${BASE_URL}img/${recipe.recipe.r_img}.jpg"
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()


            holder.ivRecipe.load(imgUri){
                addHeader("Authorization", "Bearer $APITOKEN")
                error(R.drawable.defaultimg)
            }
        }

        holder.cvRecipe.setOnClickListener {
            holder.itemView.findNavController().navigate(RecipesFragmentDirections.actionFragmentRecipesToFragmentRecipeDetails(recipe.recipe.r_title, recipe.recipe.r_id))
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}