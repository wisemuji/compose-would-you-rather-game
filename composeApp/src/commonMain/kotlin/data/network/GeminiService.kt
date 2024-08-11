package data.network

import data.network.model.GeminiResponse


interface GeminiService {
    suspend fun generateContent(content: String): GeminiResponse
}
