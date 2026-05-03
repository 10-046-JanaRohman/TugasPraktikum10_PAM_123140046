package com.example.package_123140046.viewmodel

import androidx.lifecycle.ViewModel
import com.example.package_123140046.data.model.Todo
import com.example.package_123140046.data.model.TodoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TodoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<TodoUiState>(TodoUiState())
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()

    private var nextId = 1

    // Update input text
    fun onInputChange(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    // Add new todo
    fun addTodo() {
        val currentInput = _uiState.value.inputText.trim()
        if (currentInput.isEmpty()) return

        _uiState.update { currentState ->
            currentState.copy(
                todos = currentState.todos + Todo(
                    id = nextId++,
                    text = currentInput
                ),
                inputText = "" // Reset input setelah tambah
            )
        }
    }

    // Toggle done/undone
    fun toggleTodo(id: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                todos = currentState.todos.map { todo ->
                    if (todo.id == id) todo.copy(isDone = !todo.isDone)
                    else todo
                }
            )
        }
    }

    // Delete todo
    fun deleteTodo(id: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                todos = currentState.todos.filter { it.id != id }
            )
        }
    }
}