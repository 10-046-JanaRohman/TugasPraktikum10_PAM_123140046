package com.example.package_123140046.data.local

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

actual class SettingsFactory(private val context: Context) {
    actual fun createSettings(): Settings {
        return SharedPreferencesSettings(
            delegate = context.getSharedPreferences("notesapp_settings", Context.MODE_PRIVATE)
        )
    }
}
