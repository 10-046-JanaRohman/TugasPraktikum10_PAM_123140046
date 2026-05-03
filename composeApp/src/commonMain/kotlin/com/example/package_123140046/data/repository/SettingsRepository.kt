package com.example.package_123140046.data.repository

import com.example.package_123140046.data.local.SettingsFactory
import com.example.package_123140046.data.model.AppTheme
import com.example.package_123140046.data.model.SortOrder
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepository(
    settingsFactory: SettingsFactory
) {
    private val settings: Settings = settingsFactory.createSettings()

    private companion object {
        const val KEY_APP_THEME = "app_theme"
        const val KEY_SORT_ORDER = "sort_order"
    }

    private val _appTheme = MutableStateFlow(
        runCatching {
            AppTheme.valueOf(settings.getString(KEY_APP_THEME, AppTheme.SYSTEM.name))
        }.getOrDefault(AppTheme.SYSTEM)
    )

    private val _sortOrder = MutableStateFlow(
        runCatching {
            SortOrder.valueOf(settings.getString(KEY_SORT_ORDER, SortOrder.NEWEST.name))
        }.getOrDefault(SortOrder.NEWEST)
    )

    val appTheme: StateFlow<AppTheme> = _appTheme.asStateFlow()
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    fun setAppTheme(theme: AppTheme) {
        settings.putString(KEY_APP_THEME, theme.name)
        _appTheme.value = theme
    }

    fun setSortOrder(sortOrder: SortOrder) {
        settings.putString(KEY_SORT_ORDER, sortOrder.name)
        _sortOrder.value = sortOrder
    }
}