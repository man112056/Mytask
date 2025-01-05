package com.manish.mytask

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 4, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}