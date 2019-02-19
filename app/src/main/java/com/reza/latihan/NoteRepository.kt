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
        UpdateNoteAsyncTask(noteDao).execute(note)
    }

    fun delete(note: Note) {
        DeleteNoteAsyncTask(noteDao).execute(note)
    }

    fun deleteAllNotes() {
        DeleteAllNotesAsyncTask(noteDao).execute()
    }


    private class InsertNoteAsyncTask internal constructor(private val noteDao: NoteDao) :
        AsyncTask<Note, Void, Void>() {

        override fun doInBackground(vararg notes: Note): Void? {
            noteDao.insert(notes[0])
            return null
        }
    }

    private class UpdateNoteAsyncTask internal constructor(private val noteDao: NoteDao) :
        AsyncTask<Note, Void, Void>() {

        override fun doInBackground(vararg notes: Note): Void? {
            noteDao.update(notes[0])
            return null
        }
    }

    private class DeleteNoteAsyncTask internal constructor(private val noteDao: NoteDao) :
        AsyncTask<Note, Void, Void>() {

        override fun doInBackground(vararg notes: Note): Void? {
            noteDao.delete(notes[0])
            return null
        }
    }

    private class DeleteAllNotesAsyncTask internal constructor(private val noteDao: NoteDao) :
        AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {
            noteDao.deleteAllNotes()
            return null
        }
    }
}
