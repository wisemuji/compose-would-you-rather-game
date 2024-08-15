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

package data.network.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

@Serializable
data class GeminiResponse(
    val candidates: List<CandidateResponse>,
)

@Serializable
data class CandidateResponse(
    val content: ContentResponse,
)

@Serializable
data class ContentResponse(
    val parts: List<PartResponse>,
    val role: String,
)

@Serializable
data class PartResponse(
    @Serializable(with = GameResponseSerializer::class)
    val text: GameResponse,
)

@Serializable
data class GameResponse(
    val remainingTurns: Int,
    val comment: String,
    val optionA: String?,
    val optionB: String?,
) {
    val isGameOver: Boolean
        get() = remainingTurns == 0
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = GameResponse::class)
object GameResponseSerializer : KSerializer<GameResponse> {

    override fun serialize(encoder: Encoder, value: GameResponse) {
        encoder.encodeString(Json.encodeToString(value))
    }

    override fun deserialize(decoder: Decoder): GameResponse {
        return decoder
            .decodeString()
            .removeCodeFormat()
            .let(Json::decodeFromString)
    }

    private fun String.removeCodeFormat(): String {
        return this
            .replace("```json", "")
            .replace("```", "")
    }
}
