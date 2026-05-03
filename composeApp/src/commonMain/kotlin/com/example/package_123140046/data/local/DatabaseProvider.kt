package com.example.package_123140046.data.local

import com.example.package_123140046.database.NotesDatabase

class DatabaseProvider(driverFactory: DatabaseDriverFactory) {
    val database: NotesDatabase = NotesDatabase(driverFactory.createDriver())
}
