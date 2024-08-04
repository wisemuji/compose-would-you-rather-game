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
