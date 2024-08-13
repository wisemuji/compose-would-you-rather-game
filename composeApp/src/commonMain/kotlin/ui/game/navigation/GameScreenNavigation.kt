package ui.game.navigation

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ui.LocalNavAnimatedVisibilityScope
import ui.game.GameScreen
import ui.navigation.Screen
import ui.result.GameResult

fun NavController.navigateToGame() {
    popBackStack()
    navigate(Screen.Game)
}

fun NavGraphBuilder.gameScreen(
    onShowResult: (GameResult) -> Unit,
) {
    composable<Screen.Game> {
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            GameScreen(
                onShowResult = onShowResult,
            )
        }
    }
}
