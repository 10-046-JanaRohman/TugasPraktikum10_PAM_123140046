package com.example.package_123140046.ai

sealed class AIError(message: String) : Exception(message) {
    class MissingApiKey : AIError("API key Gemini belum diatur di local.properties.")
    class Unauthorized : AIError("API key Gemini tidak valid atau tidak memiliki akses.")
    class RateLimited : AIError("Kuota/rate limit Gemini API habis. Coba lagi beberapa saat.")
    class ServerError : AIError("Server AI sedang bermasalah. Coba lagi nanti.")
    class NetworkError : AIError("Tidak ada koneksi internet atau request gagal.")
    class EmptyResponse : AIError("AI tidak mengembalikan jawaban.")
    class Unknown(message: String) : AIError(message)
}