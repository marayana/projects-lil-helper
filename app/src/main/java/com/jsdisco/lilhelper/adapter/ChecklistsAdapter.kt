package com.jsdisco.lilhelper.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jsdisco.lilhelper.R
import com.jsdisco.lilhelper.data.models.ChecklistAdapterItem
import com.jsdisco.lilhelper.data.models.ChecklistItem
import java.util.*


class ChecklistsAdapter(
    private val context: Context,
    private val deleteItems: (UUID, Int) -> Unit,
    private val toggleCheckbox: (item: ChecklistItem) -> Unit
) : RecyclerView.Adapter<ChecklistsAdapter.ItemViewHolder>() {

    private var dataset: List<ChecklistAdapterItem> = emptyList()

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvTitle: TextView = view.findViewById(R.id.tv_item_checklists_title)
        val ibDeleteList: ImageButton = view.findViewById(R.id.ib_item_checklist_delete)
        //val llChecklistItems: LinearLayout = view.findViewById(R.id.ll_item_checklists_items)
        val rvItems: RecyclerView = view.findViewById(R.id.rv_item_checklist_list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.list_item_checklist, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val checklist = dataset[position]

        holder.tvTitle.text = checklist.list_title

        holder.rvItems.adapter = ChecklistsListsAdapter(checklist.list_items, toggleCheckbox)

        holder.ibDeleteList.setOnClickListener {
            deleteItems(checklist.list_id, position)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(items: List<ChecklistAdapterItem>) {
        this.dataset = items

        notifyDataSetChanged()
    }

}