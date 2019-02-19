package com.reza.latihan

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQ_ADD_NOTE = 1
        const val REQ_EDIT_NOTE = 2
    }

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = NoteAdapter {
            val intent = Intent(this@MainActivity, EditNoteActivity::class.java)
                    .apply {
                        putExtra(EditNoteActivity.EXTRA_ID, it.id)
                        putExtra(EditNoteActivity.EXTRA_TITLE, it.title)
                        putExtra(EditNoteActivity.EXTRA_DESCRIPTION, it.description)
                        putExtra(EditNoteActivity.EXTRA_PRIORITY, it.priority)
                    }
            startActivityForResult(intent, REQ_EDIT_NOTE)
        }

        rv_notes.layoutManager = LinearLayoutManager(this)
        rv_notes.setHasFixedSize(true)
        rv_notes.adapter = adapter

        btn_add_note.setOnClickListener {
            val intent = Intent(this@MainActivity, EditNoteActivity::class.java)
            startActivityForResult(intent, REQ_ADD_NOTE)
        }

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        noteViewModel.allNotes.observe(this, Observer<List<Note>> {
            if (it != null)
                adapter.submitList(it)
        })

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(this@MainActivity, "Note deleted.", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(rv_notes)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_ADD_NOTE && resultCode == Activity.RESULT_OK && data != null) {
            val title = data.getStringExtra(EditNoteActivity.EXTRA_TITLE)
            val description = data.getStringExtra(EditNoteActivity.EXTRA_DESCRIPTION)
            val priority = data.getIntExtra(EditNoteActivity.EXTRA_PRIORITY, 1)

            val note = Note(title, description, priority)
            noteViewModel.insert(note) // save operation in the ViewModel

            Toast.makeText(this, "Note saved.", Toast.LENGTH_SHORT).show()
        } else if (requestCode == REQ_EDIT_NOTE && resultCode == RESULT_OK && data != null) {
            val id = data.getIntExtra(EditNoteActivity.EXTRA_ID, -1)
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated.", Toast.LENGTH_SHORT).show()
                return
            }

            val title = data.getStringExtra(EditNoteActivity.EXTRA_TITLE)
            val description = data.getStringExtra(EditNoteActivity.EXTRA_DESCRIPTION)
            val priority = data.getIntExtra(EditNoteActivity.EXTRA_PRIORITY, 1)

            val note = Note(title, description, priority)
            note.id = id
            noteViewModel.update(note)

            Toast.makeText(this, "Note updated.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Note not saved.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_notes -> {
                noteViewModel.deleteAllNotes()
                Toast.makeText(this, "All notes deleted.", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
