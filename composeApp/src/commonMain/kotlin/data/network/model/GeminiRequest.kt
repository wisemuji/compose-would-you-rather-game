package data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO: add a docs text to inform this is "only content/safetySettings impl for Gemini API Request"
@Serializable
data class RequestBody(
    val contents: List<Content>,
    val safetySettings: List<SafetySetting>,
) {
    @Serializable
    data class Content(
        val role: Role,
        val parts: List<Part>,
    ) {
        @Serializable
        enum class Role {
            @SerialName("model")
            MODEL,

            @SerialName("user")
            USER,
        }

        @Serializable
        data class Part(
            val text: String? = null,
            val inlineData: RequestInlineData? = null,
        ) {
            @Serializable
            data class RequestInlineData(
                @SerialName("mimeType") val mimeType: String,
                @SerialName("data") val data: String,
            )
        }
    }

    @Serializable
    data class SafetySetting(
        val category: Category,
        val threshold: Threshold,
    ) {
        enum class Category {
            @SerialName("HARM_CATEGORY_HATE_SPEECH")
            HARM_CATEGORY_HATE_SPEECH,

            @SerialName("HARM_CATEGORY_SEXUALLY_EXPLICIT")
            HARM_CATEGORY_SEXUALLY_EXPLICIT,

            @SerialName("HARM_CATEGORY_DANGEROUS_CONTENT")
            HARM_CATEGORY_DANGEROUS_CONTENT,

            @SerialName("HARM_CATEGORY_HARASSMENT")
            HARM_CATEGORY_HARASSMENT;
        }

        enum class Threshold {
            @SerialName("HARM_BLOCK_THRESHOLD_UNSPECIFIED")
            HARM_BLOCK_THRESHOLD_UNSPECIFIED,

            @SerialName("BLOCK_LOW_AND_ABOVE")
            BLOCK_LOW_AND_ABOVE,

            @SerialName("BLOCK_MEDIUM_AND_ABOVE")
            BLOCK_MEDIUM_AND_ABOVE,

            @SerialName("BLOCK_ONLY_HIGH")
            BLOCK_ONLY_HIGH,

            @SerialName("BLOCK_NONE")
            BLOCK_NONE,
        }

        companion object {
            val allSettingsNoneBlocked: List<SafetySetting> = Category.entries
                .map { category -> SafetySetting(category, Threshold.BLOCK_NONE) }
        }
    }
}
