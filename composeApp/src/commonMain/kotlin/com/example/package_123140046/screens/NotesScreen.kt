package com.example.package_123140046.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.package_123140046.data.model.Note

@Composable
fun NotesScreen(
    notes: List<Note>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onNoteClick: (Long) -> Unit,
    onToggleFavorite: (Note) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NetworkStatusIndicator()

        Text(
            text = "Notes App",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Cari notes") },
            singleLine = true
        )

        if (notes.isEmpty()) {
            Text(
                text = if (searchQuery.isBlank()) "Belum ada note. Tekan + untuk menambah." else "Note tidak ditemukan.",
                modifier = Modifier.padding(top = 24.dp)
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(notes, key = { it.id }) { note ->
                    NoteCard(
                        note = note,
                        onNoteClick = onNoteClick,
                        onToggleFavorite = onToggleFavorite
                    )
                }
            }
        }
    }
}

@Composable
fun NoteCard(
    note: Note,
    onNoteClick: (Long) -> Unit,
    onToggleFavorite: (Note) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNoteClick(note.id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(note.title.ifBlank { "Tanpa Judul" }, fontWeight = FontWeight.SemiBold)
                Text(note.content.ifBlank { "Tidak ada isi" })
            }

            IconButton(onClick = { onToggleFavorite(note) }) {
                Icon(
                    imageVector = if (note.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite"
                )
            }
        }
    }
}
