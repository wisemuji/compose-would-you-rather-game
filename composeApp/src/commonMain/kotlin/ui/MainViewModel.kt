package ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import model.Option
import model.TurnResult
import ui.GameUiState.GameOver
import ui.GameUiState.SelectableOptions

class MainViewModel(
    private val gameRepository: GameRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<GameUiState> = MutableStateFlow(GameUiState.Loading)
    val uiState: StateFlow<GameUiState> = _uiState

    fun startGame() {
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
                    val selectedOption = GameOver.SelectedOption(
                        option = option,
                        comment = if (option == Option.A) uiState.optionA else uiState.optionB,
                    )
                    GameOver(
                        selectedOption = selectedOption,
                        lesson = turnResult.lesson,
                    )
                }
            }
        }
    }
}
