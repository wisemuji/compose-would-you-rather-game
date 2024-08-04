package model

sealed interface TurnResult {
    data class SelectableOptions(
        val remainingTurns: Int,
        val question: String,
        val optionA: String,
        val optionB: String,
    ) : TurnResult

    data class GameOver(
        val lesson: String,
    ) : TurnResult
}
