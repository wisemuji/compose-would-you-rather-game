package data.repository

import data.network.GeminiService
import data.network.model.GameResponse
import model.Option
import model.TurnResult

class DefaultGameRepository(
    private val service: GeminiService,
) : GameRepository {

    override suspend fun startGame(): TurnResult {
        val response = service
            .generateContent(GAME_START_COMMAND)
        val gameResponse = response.candidates.first().content.parts.first().text
        return gameResponse.toTurnResult()
    }

    override suspend fun selectOption(option: Option): TurnResult {
        val response = service.generateContent(option.toGeminiOption())
        val gameResponse = response.candidates.first().content.parts.first().text
        return gameResponse.toTurnResult()
    }

    private fun Option.toGeminiOption(): String {
        return when (this) {
            Option.A -> "A"
            Option.B -> "B"
        }
    }

    private fun GameResponse.toTurnResult(): TurnResult {
        return if (isGameOver) {
            TurnResult.GameOver(
                lesson = comment,
            )
        } else {
            TurnResult.SelectableOptions(
                remainingTurns = remainingTurns,
                question = comment,
                optionA = optionA ?: "",
                optionB = optionB ?: "",
            )
        }
    }

    companion object {
        // TODO: raw string literal 관리
        private const val GAME_START_COMMAND = "게임 시작"
    }
}
