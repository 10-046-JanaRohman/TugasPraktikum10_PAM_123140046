package com.example.package_123140046.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.package_123140046.ai.AIRepository
import com.example.package_123140046.ai.AiChatMessage
import com.example.package_123140046.ai.AiUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val aiRepository: AIRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AiUiState(
            messages = listOf(
                AiChatMessage(
                    text = "Halo! Saya AI Notes Assistant. Saya bisa membantu merangkum, merapikan, atau membuat ide catatan.",
                    isUser = false
                )
            )
        )
    )

    val uiState: StateFlow<AiUiState> = _uiState.asStateFlow()

    fun sendMessage(message: String) {
        val cleanMessage = message.trim()

        if (cleanMessage.isBlank()) return
        if (_uiState.value.isLoading) return

        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + AiChatMessage(cleanMessage, isUser = true),
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            aiRepository.chat(cleanMessage)
                .onSuccess { response ->
                    _uiState.value = _uiState.value.copy(
                        messages = _uiState.value.messages + AiChatMessage(response, isUser = false),
                        isLoading = false,
                        errorMessage = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Terjadi kesalahan saat menghubungi AI."
                    )
                }
        }
    }

    fun clearChat() {
        aiRepository.clearConversation()
        _uiState.value = AiUiState(
            messages = listOf(
                AiChatMessage(
                    text = "Percakapan dibersihkan. Silakan mulai chat baru.",
                    isUser = false
                )
            )
        )
    }
}