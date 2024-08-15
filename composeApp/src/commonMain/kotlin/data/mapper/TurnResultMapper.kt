package data.mapper

import data.network.model.GeminiResponse
import model.TurnResult

fun GeminiResponse.toTurnResult(): TurnResult {
    val gameResponse = candidates.first().content.parts.first().text
    return gameResponse.run {
        if (isGameOver) {
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
}
