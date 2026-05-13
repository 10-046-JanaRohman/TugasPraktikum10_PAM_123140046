package com.example.package_123140046.viewmodel

import app.cash.turbine.test
import com.example.package_123140046.data.model.Note
import com.example.package_123140046.data.model.SortOrder
import com.example.package_123140046.data.repository.NoteRepository
import com.example.package_123140046.data.repository.SettingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelTest {

    private val noteRepository = mockk<NoteRepository>(relaxed = true)
    private val settingsRepository = mockk<SettingsRepository>(relaxed = true)
    private lateinit var viewModel: NotesViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { settingsRepository.sortOrder } returns MutableStateFlow(SortOrder.NEWEST)
        coEvery { noteRepository.getNotes(any(), any()) } returns flowOf(emptyList())
        coEvery { noteRepository.getFavoriteNotes() } returns flowOf(emptyList())

        viewModel = NotesViewModel(noteRepository, settingsRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial notes state should be empty`() = runTest {
        viewModel.notes.test {
            assertEquals(0, awaitItem().size)
        }
    }

    @Test
    fun `updateSearchQuery should change query state`() = runTest {
        val query = "Cari Catatan"
        viewModel.updateSearchQuery(query)
        assertEquals(query, viewModel.getSearchQuery().value)
    }

    @Test
    fun `addNote should trigger repository insert`() = runTest {
        viewModel.addNote("Judul Baru", "Isi Catatan")
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { noteRepository.insertNote("Judul Baru", "Isi Catatan") }
    }

    @Test
    fun `updateNote should trigger repository update`() = runTest {
        viewModel.updateNote(1L, "Judul Edit", "Isi Edit")
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { noteRepository.updateNote(1L, "Judul Edit", "Isi Edit") }
    }

    @Test
    fun `toggleFavorite should trigger repository update`() = runTest {
        val note = Note(1L, "Judul", "Isi", isFavorite = false, createdAt = 0L, updatedAt = 0L)
        viewModel.toggleFavorite(note)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { noteRepository.toggleFavorite(note) }
    }

    @Test
    fun `deleteNote should trigger repository delete`() = runTest {
        viewModel.deleteNote(1L)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { noteRepository.deleteNote(1L) }
    }

    @Test
    fun `favoriteNotes should emit updates from repository`() = runTest {
        val favoritesFlow = MutableStateFlow<List<Note>>(emptyList())
        coEvery { noteRepository.getFavoriteNotes() } returns favoritesFlow

        val customViewModel = NotesViewModel(noteRepository, settingsRepository)

        customViewModel.favoriteNotes.test {
            assertEquals(emptyList<Note>(), awaitItem()) 

            val newFavorites = listOf(Note(1L, "Fav 1", "Content", true, 0L, 0L))
            favoritesFlow.value = newFavorites

            assertEquals(newFavorites, awaitItem())
        }
    }

    @Test
    fun `notes should emit new list when search query changes`() = runTest {
        val notesForEmptyQuery = listOf(Note(1L, "Note 1", "Content", false, 0L, 0L))
        val notesForSpecificQuery = listOf(Note(2L, "Cari", "Hasil", false, 0L, 0L))

        coEvery { noteRepository.getNotes(any(), "") } returns flowOf(notesForEmptyQuery)
        coEvery { noteRepository.getNotes(any(), "Cari") } returns flowOf(notesForSpecificQuery)

        viewModel.notes.test {
            assertEquals(emptyList<Note>(), awaitItem()) 
            assertEquals(notesForEmptyQuery, awaitItem()) 

            viewModel.updateSearchQuery("Cari")

            assertEquals(notesForSpecificQuery, awaitItem())
        }
    }
}
