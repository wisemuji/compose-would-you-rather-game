package ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.GameRepository
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import model.Option
import model.TurnResult
import ui.game.GameUiState.SelectableOptions
import ui.result.GameResult

@Serializable
sealed interface GameUiState {
    data object LoadingGame : GameUiState

    data class SelectableOptions(
        val remainingTurns: Int,
        val question: String,
        val optionA: String,
        val optionB: String,
        val isLoadingOptions: Boolean = false,
    ) : GameUiState {
        companion object {
            fun fromTurnResult(turnResult: TurnResult.SelectableOptions): SelectableOptions =
                SelectableOptions(
                    remainingTurns = turnResult.remainingTurns,
                    question = turnResult.question,
                    optionA = turnResult.optionA,
                    optionB = turnResult.optionB,
                )
        }
    }
}

class GameViewModel(
    private val gameRepository: GameRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<GameUiState> = MutableStateFlow(GameUiState.LoadingGame)
    val uiState: StateFlow<GameUiState> = _uiState

    private val _showResult: MutableStateFlow<GameResult?> = MutableStateFlow(null)
    val showResult: StateFlow<GameResult?> = _showResult

    private val _showError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showError: StateFlow<Boolean> = _showError

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        when (throwable) {
            is JsonConvertException, is IllegalArgumentException, is IllegalStateException ->
                _showError.value = true
        }
    }

    fun startGame() {
        _uiState.value = GameUiState.LoadingGame

        viewModelScope.launch(exceptionHandler) {
            val turnResult = gameRepository.startGame()
            _uiState.value = when (turnResult) {
                is TurnResult.SelectableOptions -> SelectableOptions.fromTurnResult(turnResult)
                is TurnResult.GameOver -> throw IllegalStateException("Game should not be over at the starting state.")
            }
        }
    }

    fun selectOption(option: Option) {
        if ((uiState.value as SelectableOptions).isLoadingOptions) return
        _uiState.value = (uiState.value as SelectableOptions).copy(isLoadingOptions = true)

        viewModelScope.launch(exceptionHandler) {
            val turnResult = gameRepository.selectOption(option)

            if (turnResult is TurnResult.GameOver) {
                val uiState = uiState.value as SelectableOptions
                _showResult.value = GameResult(
                    optionComment = if (option == Option.A) uiState.optionA else uiState.optionB,
                    lesson = turnResult.lesson,
                )
            } else if (turnResult is TurnResult.SelectableOptions) {
                _uiState.value = SelectableOptions(
                    remainingTurns = turnResult.remainingTurns,
                    question = turnResult.question,
                    optionA = turnResult.optionA,
                    optionB = turnResult.optionB,
                )
            }
        }
    }
}
