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

package data.mapper

import data.network.model.GeminiResponse
import model.TurnResult

fun GeminiResponse.toTurnResult(): TurnResult {
    val gameResponse = candidates.first().content.parts.first().text
    return gameResponse.run {
        if (isGameOver) {
            TurnResult.GameOver(
                lesson = comment,
            )
        } else {
            TurnResult.SelectableOptions(
                remainingTurns = remainingTurns,
                question = comment,
                optionA = optionA ?: "",
                optionB = optionB ?: "",
            )
        }
    }
}
