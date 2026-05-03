package com.example.package_123140046.viewmodel

import androidx.lifecycle.ViewModel
import com.example.package_123140046.data.model.AppTheme
import com.example.package_123140046.data.model.SortOrder
import com.example.package_123140046.data.repository.SettingsRepository

class SettingsViewModel(
    private val repository: SettingsRepository
) : ViewModel() {

    val appTheme = repository.appTheme
    val sortOrder = repository.sortOrder

    fun setAppTheme(theme: AppTheme) {
        repository.setAppTheme(theme)
    }

    fun setSortOrder(sortOrder: SortOrder) {
        repository.setSortOrder(sortOrder)
    }
}