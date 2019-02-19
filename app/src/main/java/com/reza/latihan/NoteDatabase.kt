package com.reza.latihan

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.os.AsyncTask

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {

        // https://developer.android.com/training/data-storage/room/

        @Volatile
        private var instance: NoteDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java, "note_database"
            )
                .fallbackToDestructiveMigration()
                .addCallback(roomCallback) // executed on database first creation
                .build()

        private val roomCallback = object : RoomDatabase.Callback() {
            // CTRL + O
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(instance!!).execute()
            }
        }
    }


    private class PopulateDbAsyncTask internal constructor(db: NoteDatabase) :
        AsyncTask<Void, Void, Void>() {

        private val noteDao: NoteDao = db.noteDao()

        override fun doInBackground(vararg voids: Void): Void? {
            noteDao.insert(Note("Title 1", "Description 1", 1))
            noteDao.insert(Note("Title 2", "Description 2", 2))
            noteDao.insert(Note("Title 3", "Description 3", 3))
            return null
        }
    }
}
