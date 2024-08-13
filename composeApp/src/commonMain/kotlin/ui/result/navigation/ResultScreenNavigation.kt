package ui.result.navigation

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ui.LocalNavAnimatedVisibilityScope
import ui.navigation.GameResultNavType
import ui.navigation.Screen
import ui.result.GameResult
import ui.result.ResultScreen

fun NavController.navigateToResult(gameResult: GameResult) {
    popBackStack()
    navigate(Screen.Result(gameResult))
}

fun NavGraphBuilder.resultScreen(
    onRestart: () -> Unit,
) {
    composable<Screen.Result>(
        typeMap = GameResultNavType.TYPE_MAP
    ) { backStackEntry ->
        val args: Screen.Result = backStackEntry.toRoute<Screen.Result>()
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            ResultScreen(
                gameResult = args.gameResult,
                onRestart = onRestart,
            )
        }
    }
}
