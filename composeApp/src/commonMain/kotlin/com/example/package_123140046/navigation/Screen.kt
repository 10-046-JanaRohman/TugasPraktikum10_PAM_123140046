package com.example.package_123140046.navigation

sealed class Screen(val route: String) {
    data object Notes : Screen("notes")
    data object Favorites : Screen("favorites")
    data object Assistant : Screen("assistant")
    data object Profile : Screen("profile")
    data object AddNote : Screen("add_note")

    data object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Long): String {
            return "note_detail/$noteId"
        }
    }

    data object EditNote : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Long): String {
            return "edit_note/$noteId"
        }
    }
}