package ui

import model.Option

sealed interface GameUiState {
    // TODO: error handling
    data object Loading : GameUiState

    data class SelectableOptions(
        val remainingTurns: Int,
        val question: String,
        val optionA: String,
        val optionB: String,
    ) : GameUiState

    data class GameOver(
        val selectedOption: SelectedOption,
        val lesson: String,
    ) : GameUiState {
        data class SelectedOption(
            val comment: String,
            val option: Option,
        )
    }
}
