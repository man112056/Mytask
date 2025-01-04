package com.manish.mytask.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manish.mytask.Note
import com.manish.mytask.NoteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

open class NoteViewModel(private val noteDao: NoteDao) : ViewModel() {

    val notes: Flow<List<Note>> = noteDao.getAllNotes()
    fun addNote(note: Note) {
        viewModelScope.launch {
            noteDao.insert(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteDao.delete(note)
        }
    }
}