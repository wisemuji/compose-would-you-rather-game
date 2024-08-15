package ui.navigation

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import net.thauvin.erik.urlencoder.UrlEncoderUtil
import ui.result.GameResult
import kotlin.reflect.typeOf

class GameResultNavType : NavType<GameResult>(isNullableAllowed = false) {

    override fun serializeAsValue(value: GameResult): String {
        val jsonEncoded = Json.encodeToString(
            GameResult.serializer(),
            value.copy(
                optionComment = UrlEncoderUtil.encode(value.optionComment),
                lesson = UrlEncoderUtil.encode(value.lesson),
            )
        )
        return jsonEncoded
    }

    override fun get(bundle: Bundle, key: String): GameResult? {
        return bundle.getString(key)
            ?.let(::parseValue)
    }

    override fun parseValue(value: String): GameResult {
        val jsonDecoded = Json.decodeFromString(GameResult.serializer(), value)
        return jsonDecoded
            .copy(
                optionComment = UrlEncoderUtil.decode(jsonDecoded.optionComment),
                lesson = UrlEncoderUtil.decode(jsonDecoded.lesson),
            )
    }

    override fun put(bundle: Bundle, key: String, value: GameResult) {
        bundle.putString(key, serializeAsValue(value))
    }

    companion object {
        val TYPE_MAP = mapOf(typeOf<GameResult>() to GameResultNavType())
    }
}
