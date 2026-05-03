package com.example.package_123140046.ai

import com.example.package_123140046.platform.ApiConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.delay

class GeminiService(
    private val client: HttpClient
) {
    private val baseUrl = "https://generativelanguage.googleapis.com/v1beta"
    private val model = "gemini-2.5-flash"

    suspend fun generateContent(prompt: String): Result<String> {
        return safeAICall {
            retryWithBackoff {
                requestGemini(prompt)
            }
        }
    }

    private suspend fun requestGemini(prompt: String): String {
        val apiKey = ApiConfig.geminiApiKey

        if (apiKey.isBlank()) {
            throw AIError.MissingApiKey()
        }

        val requestJson = """
            {
              "systemInstruction": {
                "parts": [
                  {
                    "text": "Kamu adalah AI Notes Assistant di aplikasi NotesApp. Jawab dalam Bahasa Indonesia, singkat, jelas, dan membantu pengguna membuat, merapikan, atau merangkum catatan."
                  }
                ]
              },
              "contents": [
                {
                  "role": "user",
                  "parts": [
                    {
                      "text": "${prompt.escapeJson()}"
                    }
                  ]
                }
              ],
              "generationConfig": {
                "temperature": 0.7,
                "maxOutputTokens": 1000,
                "topP": 0.95
              }
            }
        """.trimIndent()

        val responseText: String = client.post("$baseUrl/models/$model:generateContent") {
            contentType(ContentType.Application.Json)
            parameter("key", apiKey)
            setBody(requestJson)
        }.body()

        if (responseText.contains("\"error\"")) {
            val errorMessage = extractJsonString(responseText, "message")

            if (
                errorMessage.contains("quota", ignoreCase = true) ||
                errorMessage.contains("rate", ignoreCase = true) ||
                errorMessage.contains("limit", ignoreCase = true)
            ) {
                throw AIError.RateLimited()
            }

            throw AIError.Unknown(errorMessage.ifBlank { "Terjadi error dari Gemini API." })
        }

        val aiText = extractJsonString(responseText, "text")

        if (aiText.isBlank()) {
            throw AIError.EmptyResponse()
        }

        return aiText
    }

    private fun extractJsonString(json: String, key: String): String {
        val keyPattern = "\"$key\""
        val keyIndex = json.indexOf(keyPattern)

        if (keyIndex == -1) return ""

        val colonIndex = json.indexOf(':', startIndex = keyIndex + keyPattern.length)
        if (colonIndex == -1) return ""

        var startQuoteIndex = -1
        var i = colonIndex + 1

        while (i < json.length) {
            if (json[i] == '"') {
                startQuoteIndex = i
                break
            }
            i++
        }

        if (startQuoteIndex == -1) return ""

        val builder = StringBuilder()
        var escaped = false
        i = startQuoteIndex + 1

        while (i < json.length) {
            val char = json[i]

            if (escaped) {
                when (char) {
                    'n' -> builder.append('\n')
                    'r' -> builder.append('\r')
                    't' -> builder.append('\t')
                    '"' -> builder.append('"')
                    '\\' -> builder.append('\\')
                    else -> builder.append(char)
                }
                escaped = false
            } else {
                when (char) {
                    '\\' -> escaped = true
                    '"' -> return builder.toString()
                    else -> builder.append(char)
                }
            }

            i++
        }

        return builder.toString()
    }

    private suspend fun <T> safeAICall(block: suspend () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (error: AIError) {
            Result.failure(error)
        } catch (error: ClientRequestException) {
            when (error.response.status.value) {
                401, 403 -> Result.failure(AIError.Unauthorized())
                429 -> Result.failure(AIError.RateLimited())
                else -> Result.failure(
                    AIError.Unknown("Request tidak valid: ${error.response.status.value}")
                )
            }
        } catch (error: ServerResponseException) {
            Result.failure(AIError.ServerError())
        } catch (error: HttpRequestTimeoutException) {
            Result.failure(AIError.NetworkError())
        } catch (error: Throwable) {
            Result.failure(AIError.NetworkError())
        }
    }

    private suspend fun <T> retryWithBackoff(
        times: Int = 3,
        initialDelay: Long = 1_000,
        maxDelay: Long = 8_000,
        block: suspend () -> T
    ): T {
        var delayTime = initialDelay

        repeat(times - 1) {
            try {
                return block()
            } catch (error: AIError.MissingApiKey) {
                throw error
            } catch (error: AIError.Unauthorized) {
                throw error
            } catch (error: ClientRequestException) {
                if (error.response.status.value != 429) {
                    throw error
                }

                delay(delayTime)
                delayTime = (delayTime * 2).coerceAtMost(maxDelay)
            } catch (error: ServerResponseException) {
                delay(delayTime)
                delayTime = (delayTime * 2).coerceAtMost(maxDelay)
            }
        }

        return block()
    }

    private fun String.escapeJson(): String {
        return this
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }
}