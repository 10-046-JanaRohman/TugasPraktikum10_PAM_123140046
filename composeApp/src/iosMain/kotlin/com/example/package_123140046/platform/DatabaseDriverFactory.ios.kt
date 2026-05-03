package com.example.package_123140046.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.package_123140046.database.NotesDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = NotesDatabase.Schema,
            name = "notesapp.db"
        )
    }
}
