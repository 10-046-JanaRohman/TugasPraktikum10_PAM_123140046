package com.example.package_123140046.ai

class AIRepositoryImpl(
    private val geminiService: GeminiService
) : AIRepository {

    private val conversationHistory = mutableListOf<AiChatMessage>()

    override suspend fun chat(message: String): Result<String> {
        val prompt = buildChatPrompt(message)

        return geminiService.generateContent(prompt)
            .onSuccess { response ->
                conversationHistory.add(AiChatMessage(message, isUser = true))
                conversationHistory.add(AiChatMessage(response, isUser = false))
            }
    }

    override suspend fun summarizeNote(title: String, content: String): Result<String> {
        val prompt = """
            Kamu adalah asisten produktivitas untuk aplikasi NotesApp.

            Tugas:
            Rangkum catatan berikut dalam Bahasa Indonesia.

            Aturan:
            - Maksimal 3 kalimat.
            - Fokus pada poin utama.
            - Jika ada action item, tuliskan dengan jelas.
            - Jangan menambahkan informasi di luar isi catatan.

            Judul:
            $title

            Isi catatan:
            $content
        """.trimIndent()

        return geminiService.generateContent(prompt)
    }

    override fun clearConversation() {
        conversationHistory.clear()
    }

    private fun buildChatPrompt(userMessage: String): String {
        val historyText = conversationHistory
            .takeLast(8)
            .joinToString(separator = "\n") { message ->
                if (message.isUser) {
                    "User: ${message.text}"
                } else {
                    "Assistant: ${message.text}"
                }
            }

        return """
            Kamu adalah AI Notes Assistant di aplikasi NotesApp.

            Peran:
            - Membantu pengguna membuat, merapikan, merangkum, dan mengembangkan catatan.
            - Menjawab dalam Bahasa Indonesia yang jelas dan sopan.
            - Memberikan jawaban singkat, praktis, dan mudah diterapkan.
            - Jika pengguna meminta ide catatan, berikan poin-poin.
            - Jika pengguna meminta perbaikan tulisan, berikan versi yang lebih rapi.
            - Jangan mengaku bisa mengubah database aplikasi secara langsung.

            Riwayat percakapan:
            $historyText

            Pesan pengguna terbaru:
            $userMessage

            Jawaban:
        """.trimIndent()
    }
}