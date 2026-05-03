package com.example.package_123140046.ai

data class AiChatMessage(
    val text: String,
    val isUser: Boolean
)

data class AiUiState(
    val messages: List<AiChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)