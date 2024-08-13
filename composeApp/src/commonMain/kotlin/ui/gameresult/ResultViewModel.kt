package ui.gameresult

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

@Serializable
data class GameResult(
    val optionComment: String,
    val lesson: String,
)

data class GameResultUiState(
    val gameResult: GameResult,
)

class ResultViewModel(
    gameResult: GameResult,
) : ViewModel() {

    private val _uiState: MutableStateFlow<GameResultUiState> =
        MutableStateFlow(GameResultUiState(gameResult))
    val uiState: StateFlow<GameResultUiState> = _uiState

    private val _restart: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val restart: StateFlow<Boolean> = _restart

    fun restart() {
        _restart.value = true
    }
}
