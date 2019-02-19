package com.reza.latihan

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_note.view.*

// https://developer.android.com/reference/android/support/v7/util/DiffUtil
class NoteAdapter(private val listener: (Note) -> Unit) : ListAdapter<Note, NoteAdapter.NoteHolder>(object : DiffUtil.ItemCallback<Note>() {

    override fun areItemsTheSame(oldItem: Note, newItem: Note) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Note, newItem: Note) =
            oldItem.title == newItem.title
                    && oldItem.description == newItem.description
                    && oldItem.priority == newItem.priority
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder =
            NoteHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false))

    fun getNoteAt(position: Int): Note = getItem(position)

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.bindView(getItem(position), listener)
    }

    class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(note: Note, listener: (Note) -> Unit) {
            itemView.tv_title.text = note.title
            itemView.tv_description.text = note.description
            itemView.tv_priority.text = note.priority.toString()
            itemView.setOnClickListener { listener(note) }
        }
    }
}