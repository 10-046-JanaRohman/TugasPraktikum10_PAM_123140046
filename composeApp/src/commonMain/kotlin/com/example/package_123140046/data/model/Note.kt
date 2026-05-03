package com.example.package_123140046.data.model

data class Note(
    val id: Long,
    val title: String,
    val content: String,
    val isFavorite: Boolean = false,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)