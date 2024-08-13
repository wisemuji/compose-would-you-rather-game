package ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import di.koinAppDeclaration
import org.koin.compose.KoinApplication
import ui.game.GameScreen
import ui.gameresult.ResultScreen
import ui.navigation.GameResultNavType
import ui.navigation.Screen


val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun App(
    navController: NavHostController = rememberNavController(),
) {
    KoinApplication(application = koinAppDeclaration) {
        MaterialTheme {
            Scaffold { innerPadding ->
                SharedTransitionLayout {
                    CompositionLocalProvider(
                        LocalSharedTransitionScope provides this
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Game,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            composable<Screen.Game> {
                                CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
                                    GameScreen(
                                        navigateToResult = {
                                            navController.popBackStack()
                                            navController.navigate(Screen.Result(it))
                                        },
                                    )
                                }
                            }
                            composable<Screen.Result>(
                                typeMap = GameResultNavType.TYPE_MAP
                            ) { backStackEntry ->
                                val args: Screen.Result =
                                    backStackEntry.toRoute<Screen.Result>()
                                CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
                                    ResultScreen(
                                        gameResult = args.gameResult,
                                        navigateToGame = { navController.navigate(Screen.Game) },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
