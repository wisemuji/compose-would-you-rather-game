package data.repository

import data.mapper.toTurnResult
import data.network.GeminiService
import data.network.mapper.toGeminiCommand
import data.network.model.GeminiCommand
import data.network.model.GeminiResponse
import model.Option
import model.TurnResult

class DefaultGameRepository(
    private val service: GeminiService,
) : GameRepository {

    override suspend fun startGame(): TurnResult {
        val response: GeminiResponse = service
            .generateContent(GeminiCommand.START_GAME)
        return response.toTurnResult()
    }

    override suspend fun selectOption(option: Option): TurnResult {
        val response = service
            .generateContent(option.toGeminiCommand())
        return response.toTurnResult()
    }
}
