package com.reza.latihan

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask

class NoteRepository(application: Application) {

    private val noteDao: NoteDao
    val allNotes: LiveData<List<Note>>

    init {
        val database = NoteDatabase(application)
        noteDao = database.noteDao()
        allNotes = noteDao.allNotes()
    }

    fun insert(note: Note) {
        InsertNoteAsyncTask(noteDao).execute(note)
    }

    fun update(note: Note) {

    }

    fun delete(note: Note) {

    }

    fun deleteAllNotes() {

    }


    private class InsertNoteAsyncTask internal constructor(private val noteDao: NoteDao) :
        AsyncTask<Note, Void, Void>() {

        override fun doInBackground(vararg notes: Note): Void? {
            noteDao.insert(notes[0])
            return null
        }
    }
}
