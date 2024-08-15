package data.network

import data.network.model.GeminiCommand
import data.network.model.GeminiResponse


interface GeminiService {
    suspend fun generateContent(content: GeminiCommand): GeminiResponse
}
