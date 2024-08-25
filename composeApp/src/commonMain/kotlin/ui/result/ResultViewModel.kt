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

package ui.result

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import model.GameResult

data class GameResultUiState(
    val gameResult: GameResult,
)

class ResultViewModel(
    gameResult: GameResult,
) : ViewModel() {

    private val _uiState: MutableStateFlow<GameResultUiState> =
        MutableStateFlow(GameResultUiState(gameResult))
    val uiState: StateFlow<GameResultUiState> = _uiState

    private val _shouldRestart: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val shouldRestart: StateFlow<Boolean> = _shouldRestart

    fun restart() {
        _shouldRestart.value = true
    }
}
