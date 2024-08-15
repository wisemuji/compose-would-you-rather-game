package ui.navigation

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import com.eygraber.uri.UriCodec
import kotlin.reflect.typeOf

class URLEncodedNavType : NavType<String>(isNullableAllowed = false) {

    override fun serializeAsValue(value: String): String {
        return UriCodec.encode(value)
    }

    override fun get(bundle: Bundle, key: String): String? {
        return bundle.getString(key)
            ?.let(::parseValue)
    }

    override fun parseValue(value: String): String {
        return UriCodec.decode(value)
    }

    override fun put(bundle: Bundle, key: String, value: String) {
        bundle.putString(key, serializeAsValue(value))
    }

    companion object {
        val TYPE_MAP = mapOf(typeOf<String>() to URLEncodedNavType())
    }
}
