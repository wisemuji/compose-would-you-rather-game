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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Note: This is only content/safetySettings implementation for Gemini API Request.
 * The Gemini SDK for Kotlin Multiplatform is currently unavailable; so I generated responses through HTTP requests using Ktor.
 *
 * Also refer to caution messages in [android SDK](https://github.com/google-gemini/generative-ai-android):
 * "The Google AI SDK for Android is recommended for prototyping only."
 */
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
