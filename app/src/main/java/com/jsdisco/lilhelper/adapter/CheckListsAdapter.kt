package com.jsdisco.lilhelper.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.jsdisco.lilhelper.R
import com.jsdisco.lilhelper.data.models.CheckList
import com.jsdisco.lilhelper.data.models.Note
import com.jsdisco.lilhelper.data.models.relations.CheckListWithItems

class CheckListsAdapter(
    private var dataset: List<CheckListWithItems>,
    private val context: Context,
    private val editCheckList: (CheckListWithItems) -> Unit,
    private val deleteCheckList: (CheckListWithItems) -> Unit
) : RecyclerView.Adapter<CheckListsAdapter.ItemViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<CheckListWithItems>) {
        dataset = list
        notifyDataSetChanged()
    }


    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val cvCheckList: CardView = view.findViewById(R.id.cv_item_checklist)
        val tvCheckListTitle: TextView = view.findViewById(R.id.tv_item_checklists_title)
        val llCheckListItems: LinearLayout = view.findViewById(R.id.ll_item_checklists_items)
        val ibCheckListEdit: ImageButton = view.findViewById(R.id.ib_item_checklist_edit)
        val ibCheckListDelete: ImageButton = view.findViewById(R.id.ib_item_checklist_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_checklist, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val checkList = dataset[position]

        Log.e("Adapter size:", dataset.size.toString())

        holder.tvCheckListTitle.text = checkList.checkList.cl_title

        for (item in checkList.checkListItems){
            val cv = CheckBox(context)
            cv.text = item.cli_content
            holder.llCheckListItems.addView(cv)
        }

        holder.ibCheckListDelete.setOnClickListener { deleteCheckList(checkList) }
        holder.ibCheckListEdit.setOnClickListener { editCheckList(checkList) }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}