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
}

class MainViewModel(
    private val gameRepository: GameRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<GameUiState> = MutableStateFlow(GameUiState.Loading)
    val uiState: StateFlow<GameUiState> = _uiState

    fun startGame() {
        viewModelScope.launch {
            val turnResult = gameRepository.startGame()
            turnResult.run {
                when (this) {
                    is TurnResult.SelectableOptions -> {
                        _uiState.value = GameUiState.SelectableOptions(
                            remainingTurns = remainingTurns,
                            question = question,
                            optionA = optionA,
                            optionB = optionB,
                        )
                    }

                    is TurnResult.GameOver -> {
                        _uiState.value = GameUiState.GameOver(lesson)
                    }
                }
            }
        }
    }

    fun selectOption(option: Option) {
        viewModelScope.launch {
            val turnResult = gameRepository.selectOption(option)
            turnResult.run {
                when (this) {
                    is TurnResult.SelectableOptions -> {
                        _uiState.value = GameUiState.SelectableOptions(
                            remainingTurns = remainingTurns,
                            question = question,
                            optionA = optionA,
                            optionB = optionB,
                        )
                    }

                    is TurnResult.GameOver -> {
                        _uiState.value = GameUiState.GameOver(lesson)
                    }
                }
            }
        }
    }
}
