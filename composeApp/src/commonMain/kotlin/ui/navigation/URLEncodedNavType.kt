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

package ui.navigation

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import com.eygraber.uri.UriCodec
import kotlin.reflect.typeOf

/**
 * Note: URL encoding for Kotlin Multiplatform is not provided by default.
 * This is a workaround for sending emoji characters in URL. (Navigation Compose)
 *
 * By using external library [uri-kmp](https://github.com/eygraber/uri-kmp),
 * "%" character is not escaped by default, so we need to escape it manually.
 */
class URLEncodedNavType : NavType<String>(isNullableAllowed = false) {

    override fun serializeAsValue(value: String): String {
        return UriCodec.encode(value.escapePercent())
    }

    override fun get(bundle: Bundle, key: String): String? {
        return bundle.getString(key)
            ?.let(::parseValue)
    }

    override fun parseValue(value: String): String {
        return UriCodec.decode(value)
            .unescapePercent()
    }

    override fun put(bundle: Bundle, key: String, value: String) {
        bundle.putString(key, serializeAsValue(value))
    }

    private fun String.escapePercent(): String = replace("%", "%25")

    private fun String.unescapePercent(): String = replace("%25", "%")

    companion object {
        val TYPE_MAP = mapOf(typeOf<String>() to URLEncodedNavType())
    }
}
