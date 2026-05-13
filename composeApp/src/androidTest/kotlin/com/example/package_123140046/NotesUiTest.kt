package com.example.package_123140046

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.example.package_123140046.data.model.Note
import com.example.package_123140046.di.appModules
import com.example.package_123140046.screens.NotesScreen
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

import androidx.test.platform.app.InstrumentationRegistry
import org.koin.android.ext.koin.androidContext

import org.koin.compose.KoinContext

class NotesUiTest : KoinTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        startKoin {
            androidContext(InstrumentationRegistry.getInstrumentation().targetContext)
            modules(appModules())
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun testNotesScreenInitialState() {
        composeTestRule.setContent {
            KoinContext {
                NotesScreen(
                    notes = emptyList(),
                    searchQuery = "",
                    onSearchChange = {},
                    onNoteClick = {},
                    onToggleFavorite = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Notes App").assertIsDisplayed()
        composeTestRule.onNodeWithText("Belum ada note. Tekan + untuk menambah.")
            .assertIsDisplayed()
    }

    @Test
    fun testNotesScreenWithData() {
        val notes = listOf(
            Note(1L, "Judul Test", "Konten Test", false, 0L, 0L)
        )
        composeTestRule.setContent {
            KoinContext {
                NotesScreen(
                    notes = notes,
                    searchQuery = "",
                    onSearchChange = {},
                    onNoteClick = {},
                    onToggleFavorite = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Judul Test").assertIsDisplayed()
        composeTestRule.onNodeWithText("Konten Test").assertIsDisplayed()
    }

    @Test
    fun testSearchInput() {
        var queryValue = ""
        composeTestRule.setContent {
            KoinContext {
                NotesScreen(
                    notes = emptyList(),
                    searchQuery = queryValue,
                    onSearchChange = { queryValue = it },
                    onNoteClick = {},
                    onToggleFavorite = {}
                )
            }
        }

        val searchField = composeTestRule.onNodeWithText("Cari notes")
        searchField.performTextInput("Belajar")
    }
}
