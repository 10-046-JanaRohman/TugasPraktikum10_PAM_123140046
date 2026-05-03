package com.example.package_123140046.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Notes : BottomNavItem(
        route = Screen.Notes.route,
        label = "Notes",
        icon = Icons.Default.Home
    )

    data object Favorites : BottomNavItem(
        route = Screen.Favorites.route,
        label = "Favorites",
        icon = Icons.Default.Favorite
    )

    data object Assistant : BottomNavItem(
        route = Screen.Assistant.route,
        label = "AI",
        icon = Icons.Default.Info
    )

    data object Profile : BottomNavItem(
        route = Screen.Profile.route,
        label = "Profile",
        icon = Icons.Default.Person
    )
}