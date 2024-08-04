package data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestBody(val contents: List<Content>) {
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
    }
    @Serializable
    data class Part(
        val text: String? = null,
        val inlineData: RequestInlineData? = null
    )
    @Serializable
    data class RequestInlineData(
        @SerialName("mimeType") val mimeType: String,
        @SerialName("data") val data: String
    )
}
