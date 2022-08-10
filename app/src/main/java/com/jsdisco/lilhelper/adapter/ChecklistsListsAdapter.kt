package com.jsdisco.lilhelper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.jsdisco.lilhelper.R
import com.jsdisco.lilhelper.data.local.models.ChecklistItem

class ChecklistsListsAdapter(
    private val dataset: ArrayList<ChecklistItem>,
    private val toggleCheckbox: (ChecklistItem) -> Unit
    ) :  RecyclerView.Adapter<ChecklistsListsAdapter.ItemViewHolder>(){

        class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
            val cb: CheckBox = view.findViewById(R.id.cb_list_item_checklist_item)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.list_item_checklist_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]

        holder.cb.text = item.cli_content
        holder.cb.isChecked = item.cli_isChecked

        holder.cb.setOnClickListener {
            item.cli_isChecked = !item.cli_isChecked
            toggleCheckbox(item)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}
