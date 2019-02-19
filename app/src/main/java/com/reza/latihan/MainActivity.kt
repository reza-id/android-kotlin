package com.reza.latihan

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = NoteAdapter()

        rv_notes.layoutManager = LinearLayoutManager(this)
        rv_notes.setHasFixedSize(true)
        rv_notes.adapter = adapter

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        noteViewModel.allNotes.observe(this, Observer<List<Note>> {
            if (it != null)
                adapter.setNotes(it)
        })
    }
}
