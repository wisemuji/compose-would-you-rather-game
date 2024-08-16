/*
 * Copyright 2024 Suhyeon Kim(wisemuji), Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.GameRepository
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import model.GameResult
import model.Option
import model.TurnResult
import ui.game.GameUiState.SelectableOptions

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
            when (val turnResult = gameRepository.selectOption(option)) {
                is TurnResult.GameOver -> {
                    val uiState = uiState.value as? SelectableOptions
                        ?: throw IllegalStateException("Game should not be over before selecting an option.")
                    _showResult.value = GameResult(
                        optionComment = if (option == Option.A) uiState.optionA else uiState.optionB,
                        lesson = turnResult.lesson,
                    )
                }

                is TurnResult.SelectableOptions -> {
                    _uiState.value = SelectableOptions.fromTurnResult(turnResult)
                }
            }
        }
    }
}
