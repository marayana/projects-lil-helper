package com.jsdisco.lilhelper.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jsdisco.lilhelper.R
import com.jsdisco.lilhelper.data.local.models.Note
//import com.jsdisco.lilhelper.ui.DeleteDialog


class NotesAdapter(
    private var dataset: List<Note>,
    private val editNote: (Note) -> Unit,
    private val deleteNote: (Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.ItemViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Note>) {
        dataset = list
        notifyDataSetChanged()
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvNoteTitle: TextView = view.findViewById(R.id.tv_item_notes_title)
        val tvNoteContent: TextView = view.findViewById(R.id.tv_item_notes_content)
        val ibNoteEdit: ImageButton = view.findViewById(R.id.ib_item_note_edit)
        val ibNoteDelete: ImageButton = view.findViewById(R.id.ib_item_note_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_note, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val note = dataset[position]

        holder.tvNoteTitle.text = note.title
        holder.tvNoteContent.text = note.content

        holder.ibNoteEdit.setOnClickListener {
            editNote(note)
        }

        holder.ibNoteDelete.setOnClickListener {
            deleteNote(note)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}