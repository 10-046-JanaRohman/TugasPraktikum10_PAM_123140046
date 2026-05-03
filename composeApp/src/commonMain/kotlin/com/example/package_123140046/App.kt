package com.example.package_123140046

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.package_123140046.data.model.AppTheme
import com.example.package_123140046.navigation.AppNavigation
import com.example.package_123140046.viewmodel.SettingsViewModel
import org.koin.compose.koinInject

@Composable
fun App() {
    val settingsViewModel: SettingsViewModel = koinInject()
    val appTheme by settingsViewModel.appTheme.collectAsState()

    val useDarkTheme = when (appTheme) {
        AppTheme.SYSTEM -> isSystemInDarkTheme()
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
    }

    val colorScheme = if (useDarkTheme) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }

    MaterialTheme(colorScheme = colorScheme) {
        AppNavigation()
    }
}