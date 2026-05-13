package com.example.package_123140046.data.repository

import com.example.package_123140046.data.model.Note
import com.example.package_123140046.data.model.SortOrder
import com.example.package_123140046.database.NotesDatabase
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NoteRepositoryTest {
    private lateinit var repository: NoteRepository
    private val database = mockk<NotesDatabase>()

    // Inisialisasi queries sebagai relaxed mock di level class
    // Gunakan package yang diharapkan oleh NotesDatabase (tanpa underscore jika perlu)
    private val queries =
        mockk<com.example.package123140046.database.NoteEntityQueries>(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        // Stubbing HARUS dilakukan sebelum NoteRepository(database) dipanggil
        every { database.noteEntityQueries } returns queries

        repository = NoteRepository(database)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearMocks(queries) // Bersihkan state mock antar test
    }

    @Test
    fun testInsertNote() = runTest {
        repository.insertNote("Test Title", "Test Content")
        verify {
            queries.insert(
                title = "Test Title",
                content = "Test Content",
                is_favorite = 0L,
                created_at = any(),
                updated_at = any()
            )
        }
    }

    @Test
    fun testUpdateNote() = runTest {
        repository.updateNote(1L, "Updated Title", "Updated Content")
        verify {
            queries.update(
                title = "Updated Title",
                content = "Updated Content",
                updated_at = any(),
                id = 1L
            )
        }
    }

    @Test
    fun testDeleteNote() = runTest {
        repository.deleteNote(1L)
        verify { queries.deleteById(1L) }
    }

    @Test
    fun testToggleFavorite() = runTest {
        val note = Note(1L, "Title", "Content", isFavorite = false, createdAt = 0L, updatedAt = 0L)
        repository.toggleFavorite(note)
        verify {
            queries.toggleFavorite(
                is_favorite = 1L,
                updated_at = any(),
                id = 1L
            )
        }
    }

    @Test
    fun testGetNotesNewest() = runTest {
        repository.getNotes(SortOrder.NEWEST, "")
        // Mapper any() biasanya membutuhkan tipe eksplisit jika ada ambiguasi
        verify { queries.selectAllNewest<Note>(any()) }
    }

    @Test
    fun testSearchNotes() = runTest {
        repository.getNotes(SortOrder.NEWEST, "query")
        verify { queries.searchNewest<Note>(eq("query"), eq("query"), any()) }
    }
}