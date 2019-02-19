package com.reza.latihan

import android.app.Application
import android.arch.lifecycle.LiveData

class NoteRepository(application: Application) {

    private val noteDao: NoteDao
    val allNotes: LiveData<List<Note>>

    init {
        val database = NoteDatabase(application)
        noteDao = database.noteDao()
        allNotes = noteDao.allNotes()
    }

    fun insert(note: Note) {

    }

    fun update(note: Note) {

    }

    fun delete(note: Note) {

    }

    fun deleteAllNotes() {

    }
}
