package com.example.package_123140046.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.package_123140046.data.model.Note
import com.example.package_123140046.data.model.SortOrder
import com.example.package_123140046.database.NotesDatabase
// import com.example.package_123140046.database.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class NoteRepository(database: com.example.package_123140046.database.NotesDatabase) {

    private val queries = database.noteEntityQueries

    fun getNotes(sortOrder: SortOrder, searchQuery: String): Flow<List<Note>> {
        val query = searchQuery.trim()

        val source = when {
            query.isBlank() && sortOrder == SortOrder.NEWEST -> {
                queries.selectAllNewest(::mapToNote)
            }

            query.isBlank() && sortOrder == SortOrder.OLDEST -> {
                queries.selectAllOldest(::mapToNote)
            }

            sortOrder == SortOrder.NEWEST -> {
                queries.searchNewest(query, query, ::mapToNote)
            }

            else -> {
                queries.searchOldest(query, query, ::mapToNote)
            }
        }

        return source.asFlow().mapToList(Dispatchers.Default)
    }

    fun getFavoriteNotes(): Flow<List<Note>> {
        return queries.selectFavorites(::mapToNote)
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    suspend fun getNoteById(id: Long): Note? {
        return withContext(Dispatchers.Default) {
            queries.selectById(id, ::mapToNote).executeAsOneOrNull()
        }
    }

    suspend fun insertNote(title: String, content: String) {
        withContext(Dispatchers.Default) {
            if (title.isBlank() && content.isBlank()) return@withContext

            val now = nowMillis()

            queries.insert(
                title = title.trim(),
                content = content.trim(),
                is_favorite = 0L,
                created_at = now,
                updated_at = now
            )
        }
    }

    suspend fun updateNote(id: Long, title: String, content: String) {
        withContext(Dispatchers.Default) {
            if (title.isBlank() && content.isBlank()) return@withContext

            queries.update(
                title = title.trim(),
                content = content.trim(),
                updated_at = nowMillis(),
                id = id
            )
        }
    }

    suspend fun deleteNote(id: Long) {
        withContext(Dispatchers.Default) {
            queries.deleteById(id)
        }
    }

    suspend fun toggleFavorite(note: Note) {
        withContext(Dispatchers.Default) {
            val favoriteValue = if (note.isFavorite) 0L else 1L

            queries.toggleFavorite(
                is_favorite = favoriteValue,
                updated_at = nowMillis(),
                id = note.id
            )
        }
    }

    private fun mapToNote(
        id: Long,
        title: String,
        content: String,
        is_favorite: Long,
        created_at: Long,
        updated_at: Long
    ): Note {
        return Note(
            id = id,
            title = title,
            content = content,
            isFavorite = is_favorite == 1L,
            createdAt = created_at,
            updatedAt = updated_at
        )
    }

    private fun nowMillis(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }
}