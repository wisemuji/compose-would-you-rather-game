package ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import model.Option
import model.TurnResult
import ui.GameUiState.GameResult
import ui.GameUiState.SelectableOptions

sealed interface GameUiState {
    // TODO: error handling
    data object LoadingGame : GameUiState

    data class SelectableOptions(
        val remainingTurns: Int,
        val question: String,
        val optionA: String,
        val optionB: String,
        val isLoadingOptions: Boolean = false,
    ) : GameUiState

    data class GameResult(
        val selectedOption: SelectedOption,
        val lesson: String,
    ) : GameUiState {
        data class SelectedOption(
            val comment: String,
            val option: Option,
        )
    }
}

class MainViewModel(
    private val gameRepository: GameRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<GameUiState> = MutableStateFlow(GameUiState.LoadingGame)
    val uiState: StateFlow<GameUiState> = _uiState

    fun startGame() {
        _uiState.value = GameUiState.LoadingGame
        viewModelScope.launch {
            val turnResult = gameRepository.startGame()
            _uiState.value = when (turnResult) {
                is TurnResult.SelectableOptions -> {
                    SelectableOptions(
                        remainingTurns = turnResult.remainingTurns,
                        question = turnResult.question,
                        optionA = turnResult.optionA,
                        optionB = turnResult.optionB,
                    )
                }

                is TurnResult.GameOver -> TODO()
            }
        }
    }

    fun selectOption(option: Option) {
        if ((uiState.value as SelectableOptions).isLoadingOptions) return
        _uiState.value = (uiState.value as SelectableOptions).copy(isLoadingOptions = true)
        viewModelScope.launch {
            val turnResult = gameRepository.selectOption(option)
            _uiState.value = when (turnResult) {
                is TurnResult.SelectableOptions -> {
                    SelectableOptions(
                        remainingTurns = turnResult.remainingTurns,
                        question = turnResult.question,
                        optionA = turnResult.optionA,
                        optionB = turnResult.optionB,
                    )
                }

                is TurnResult.GameOver -> {
                    val uiState = uiState.value as SelectableOptions
                    val selectedOption = GameResult.SelectedOption(
                        option = option,
                        comment = if (option == Option.A) uiState.optionA else uiState.optionB,
                    )
                    GameResult(
                        selectedOption = selectedOption,
                        lesson = turnResult.lesson,
                    )
                }
            }
        }
    }
}
