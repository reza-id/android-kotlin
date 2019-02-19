package com.reza.latihan

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQ_ADD_NOTE = 1
    }

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = NoteAdapter()

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
                adapter.setNotes(it)
        })
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
        } else {
            Toast.makeText(this, "Note not saved.", Toast.LENGTH_SHORT).show()
        }
    }
}
