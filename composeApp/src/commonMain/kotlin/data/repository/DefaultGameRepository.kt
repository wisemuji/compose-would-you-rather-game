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

package data.repository

import data.mapper.toTurnResult
import data.network.GeminiService
import data.network.mapper.toGeminiCommand
import data.network.model.GeminiCommand
import data.network.model.GeminiResponse
import model.Option
import model.TurnResult

class DefaultGameRepository(
    private val service: GeminiService,
) : GameRepository {

    override suspend fun startGame(): TurnResult {
        val response: GeminiResponse = service
            .generateContent(GeminiCommand.START_GAME)
        return response.toTurnResult()
    }

    override suspend fun selectOption(option: Option): TurnResult {
        val response = service
            .generateContent(option.toGeminiCommand())
        return response.toTurnResult()
    }
}
