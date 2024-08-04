package ui

import data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import model.Option
import model.TurnResult
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

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
        val lesson: String,
    ) : GameUiState

    companion object {
        fun fromTurnResult(turnResult: TurnResult): GameUiState {
            return when (turnResult) {
                is TurnResult.SelectableOptions -> {
                    SelectableOptions(
                        remainingTurns = turnResult.remainingTurns,
                        question = turnResult.question,
                        optionA = turnResult.optionA,
                        optionB = turnResult.optionB,
                    )
                }

                is TurnResult.GameOver -> {
                    GameOver(lesson = turnResult.lesson)
                }
            }
        }
    }
}

class MainViewModel(
    private val gameRepository: GameRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<GameUiState> = MutableStateFlow(GameUiState.Loading)
    val uiState: StateFlow<GameUiState> = _uiState

    fun startGame() {
        viewModelScope.launch {
            val turnResult = gameRepository.startGame()
            _uiState.value = GameUiState.fromTurnResult(turnResult)
        }
    }

    fun selectOption(option: Option) {
        viewModelScope.launch {
            val turnResult = gameRepository.selectOption(option)
            _uiState.value = GameUiState.fromTurnResult(turnResult)
        }
    }
}
