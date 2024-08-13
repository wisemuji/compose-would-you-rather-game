package ui.navigation

import kotlinx.serialization.Serializable
import ui.result.GameResult

sealed interface Screen {
    @Serializable
    data object Game : Screen

    @Serializable
    data class Result(val gameResult: GameResult) : Screen
}
