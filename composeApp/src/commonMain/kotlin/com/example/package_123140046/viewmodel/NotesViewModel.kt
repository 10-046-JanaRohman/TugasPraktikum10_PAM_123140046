package com.example.package_123140046.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.package_123140046.data.model.Note
import com.example.package_123140046.data.model.SortOrder
import com.example.package_123140046.data.repository.NoteRepository
import com.example.package_123140046.data.repository.SettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModel(
    private val noteRepository: NoteRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val sortOrder: StateFlow<SortOrder> = settingsRepository.sortOrder
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SortOrder.NEWEST
        )

    val notes: StateFlow<List<Note>> = combine(sortOrder, searchQuery) { sort, query ->
        sort to query
    }.flatMapLatest { pair ->
        noteRepository.getNotes(pair.first, pair.second)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    val favoriteNotes: StateFlow<List<Note>> = noteRepository.getFavoriteNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun getSearchQuery(): StateFlow<String> {
        return searchQuery
    }

    suspend fun getNoteById(noteId: Long): Note? {
        return noteRepository.getNoteById(noteId)
    }

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            noteRepository.insertNote(title, content)
        }
    }

    fun updateNote(noteId: Long, title: String, content: String) {
        viewModelScope.launch {
            noteRepository.updateNote(noteId, title, content)
        }
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            noteRepository.deleteNote(noteId)
        }
    }

    fun toggleFavorite(note: Note) {
        viewModelScope.launch {
            noteRepository.toggleFavorite(note)
        }
    }
}