package com.example.package_123140046.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.package_123140046.data.model.Todo
import com.example.package_123140046.viewmodel.TodoViewModel

@Composable
fun TodoScreen(
    viewModel: TodoViewModel = viewModel { TodoViewModel() }
) {
    // Collect StateFlow sebagai Compose State
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "My Todos (${uiState.todos.count { it.isDone }}/${uiState.todos.size})",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input Row — State Hoisting ke ViewModel
        TodoInputRow(
            value = uiState.inputText,
            onValueChange = { viewModel.onInputChange(it) },
            onAdd = { viewModel.addTodo() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Loading indicator
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        // Todo List
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(uiState.todos, key = { it.id }) { todo ->
                TodoItemCard(
                    todo = todo,
                    onToggle = { viewModel.toggleTodo(todo.id) },
                    onDelete = { viewModel.deleteTodo(todo.id) }
                )
            }
        }
    }
}

// Stateless component — state hoisting diterapkan di sini
@Composable
fun TodoInputRow(
    value: String,
    onValueChange: (String) -> Unit,
    onAdd: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Tambah todo baru...") },
            singleLine = true
        )
        Button(
            onClick = onAdd,
            enabled = value.isNotBlank()
        ) {
            Text("Tambah")
        }
    }
}

// Stateless item card
@Composable
fun TodoItemCard(
    todo: Todo,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todo.isDone,
                onCheckedChange = { onToggle() }
            )
            Text(
                text = todo.text,
                modifier = Modifier.weight(1f),
                textDecoration = if (todo.isDone) TextDecoration.LineThrough else null
            )
            TextButton(onClick = onDelete) {
                Text("Hapus", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}