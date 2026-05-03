package com.example.package_123140046.ai

interface AIRepository {
    suspend fun chat(message: String): Result<String>
    suspend fun summarizeNote(title: String, content: String): Result<String>
    fun clearConversation()
}