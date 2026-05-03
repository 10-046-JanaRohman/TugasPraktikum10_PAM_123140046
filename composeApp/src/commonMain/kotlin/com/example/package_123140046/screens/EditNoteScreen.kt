package com.example.package_123140046.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.package_123140046.data.model.Note

@Composable
fun EditNoteScreen(
    note: Note,
    onSave: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var title by remember(note.id) { mutableStateOf(note.title) }
    var content by remember(note.id) { mutableStateOf(note.content) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Edit Note")
        Text("ID: ${note.id}")

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") }
        )

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") }
        )

        Button(onClick = { onSave(title, content) }) {
            Text("Save Changes")
        }

        Button(onClick = onBack) {
            Text("Back")
        }
    }
}