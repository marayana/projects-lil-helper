package com.jsdisco.lilhelper.adapter

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.jsdisco.lilhelper.R
import com.jsdisco.lilhelper.data.local.models.Drawing


class DrawingsAdapter(
    private var dataset: List<Drawing>,
    private val navToDrawing: (Long, String, String) -> Unit
) : RecyclerView.Adapter<DrawingsAdapter.ItemViewHolder>(){

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Drawing>) {
        dataset = list
        notifyDataSetChanged()
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val ivDrawing: ImageView = view.findViewById(R.id.iv_drawing)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_drawing, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val drawing = dataset[position]
        val bmImg = BitmapFactory.decodeFile(drawing.path)
        holder.ivDrawing.setImageBitmap(bmImg)

        holder.ivDrawing.setOnClickListener { navToDrawing(drawing.id, drawing.path, drawing.title) }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}