package com.example.package_123140046.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.package_123140046.data.model.Note

@Composable
fun FavoritesScreen(
    notes: List<Note>,
    onNoteClick: (Long) -> Unit,
    onToggleFavorite: (Note) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Favorite Notes",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        if (notes.isEmpty()) {
            Text("Belum ada note favorit")
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
