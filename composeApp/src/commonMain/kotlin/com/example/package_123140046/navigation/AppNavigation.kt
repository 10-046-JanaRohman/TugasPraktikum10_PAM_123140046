package com.example.package_123140046.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.package_123140046.data.model.Note
import com.example.package_123140046.screens.AddNoteScreen
import com.example.package_123140046.screens.ChatScreen
import com.example.package_123140046.screens.EditNoteScreen
import com.example.package_123140046.screens.FavoritesScreen
import com.example.package_123140046.screens.NoteDetailScreen
import com.example.package_123140046.screens.NotesScreen
import com.example.package_123140046.screens.ProfileScreen
import com.example.package_123140046.viewmodel.NotesViewModel
import org.koin.compose.koinInject

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val notesViewModel: NotesViewModel = koinInject()

    val notes by notesViewModel.notes.collectAsState()
    val favoriteNotes by notesViewModel.favoriteNotes.collectAsState()
    val searchQuery by notesViewModel.getSearchQuery().collectAsState()

    val bottomItems = listOf(
        BottomNavItem.Notes,
        BottomNavItem.Favorites,
        BottomNavItem.Assistant,
        BottomNavItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(Screen.Notes.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(item.label)
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            if (currentRoute == Screen.Notes.route) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.AddNote.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Note"
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Notes.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Notes.route) {
                NotesScreen(
                    notes = notes,
                    searchQuery = searchQuery,
                    onSearchChange = notesViewModel::updateSearchQuery,
                    onNoteClick = { noteId ->
                        navController.navigate(Screen.NoteDetail.createRoute(noteId))
                    },
                    onToggleFavorite = { note ->
                        notesViewModel.toggleFavorite(note)
                    }
                )
            }

            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    notes = favoriteNotes,
                    onNoteClick = { noteId ->
                        navController.navigate(Screen.NoteDetail.createRoute(noteId))
                    },
                    onToggleFavorite = { note ->
                        notesViewModel.toggleFavorite(note)
                    }
                )
            }

            composable(Screen.Assistant.route) {
                ChatScreen()
            }

            composable(Screen.Profile.route) {
                ProfileScreen()
            }

            composable(Screen.AddNote.route) {
                AddNoteScreen(
                    onSave = { title, content ->
                        notesViewModel.addNote(title, content)
                        navController.popBackStack()
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Screen.NoteDetail.route,
                arguments = listOf(
                    navArgument("noteId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getLong("noteId") ?: 0L
                var note by remember(noteId) {
                    mutableStateOf<Note?>(null)
                }

                LaunchedEffect(noteId, notes, favoriteNotes) {
                    note = notesViewModel.getNoteById(noteId)
                }

                note?.let { currentNote ->
                    NoteDetailScreen(
                        note = currentNote,
                        onBack = {
                            navController.popBackStack()
                        },
                        onEditClick = {
                            navController.navigate(Screen.EditNote.createRoute(noteId))
                        },
                        onDeleteClick = {
                            notesViewModel.deleteNote(noteId)
                            navController.popBackStack()
                        },
                        onToggleFavorite = {
                            notesViewModel.toggleFavorite(currentNote)
                        }
                    )
                }
            }

            composable(
                route = Screen.EditNote.route,
                arguments = listOf(
                    navArgument("noteId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getLong("noteId") ?: 0L
                var note by remember(noteId) {
                    mutableStateOf<Note?>(null)
                }

                LaunchedEffect(noteId) {
                    note = notesViewModel.getNoteById(noteId)
                }

                note?.let { currentNote ->
                    EditNoteScreen(
                        note = currentNote,
                        onSave = { title, content ->
                            notesViewModel.updateNote(noteId, title, content)
                            navController.popBackStack()
                        },
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}