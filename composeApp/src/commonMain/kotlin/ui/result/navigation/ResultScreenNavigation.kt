package ui.result.navigation

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ui.LocalNavAnimatedVisibilityScope
import ui.navigation.Screen
import ui.navigation.URLEncodedNavType
import ui.navigation.URLEncodedNavType.Companion.TYPE_MAP
import ui.result.GameResult
import ui.result.ResultScreen
import kotlin.reflect.typeOf

fun NavController.navigateToResult(gameResult: GameResult) {
    popBackStack()
    navigate(
        Screen.Result(
            optionComment = gameResult.optionComment,
            lesson = gameResult.lesson
        )
    )
}

fun NavGraphBuilder.resultScreen(
    onRestart: () -> Unit,
) {
    composable<Screen.Result>(
        typeMap = TYPE_MAP
    ) { backStackEntry ->
        val args: Screen.Result = backStackEntry.toRoute<Screen.Result>()
        val gameResult = GameResult(optionComment = args.optionComment, lesson = args.lesson)
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            ResultScreen(
                gameResult = gameResult,
                onRestart = onRestart,
            )
        }
    }
}
