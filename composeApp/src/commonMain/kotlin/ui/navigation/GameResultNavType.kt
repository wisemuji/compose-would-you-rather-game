package ui.navigation

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import ui.gameresult.GameResult
import kotlin.reflect.typeOf

class GameResultNavType : NavType<GameResult>(isNullableAllowed = false) {

    override fun serializeAsValue(value: GameResult): String {
        return Json.encodeToString(GameResult.serializer(), value)
    }

    override fun get(bundle: Bundle, key: String): GameResult? {
        val encoded = bundle.getString(key)
        return if (encoded != null) {
            Json.decodeFromString(GameResult.serializer(), encoded)
        } else {
            null
        }
    }

    override fun parseValue(value: String): GameResult {
        return Json.decodeFromString(GameResult.serializer(), value)
    }

    override fun put(bundle: Bundle, key: String, value: GameResult) {
        bundle.putString(key, serializeAsValue(value))
    }

    companion object {
        val TYPE_MAP = mapOf(typeOf<GameResult>() to GameResultNavType())
    }
}
