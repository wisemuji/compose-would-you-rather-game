package ui.navigation

import kotlinx.serialization.Serializable
import ui.gameresult.GameResult

sealed interface Screen {
    @Serializable
    data object Game : Screen

    @Serializable
    data class Result(val gameResult: GameResult) : Screen
}
