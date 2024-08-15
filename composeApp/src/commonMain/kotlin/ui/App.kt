package ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import di.koinAppDeclaration
import org.koin.compose.KoinApplication
import ui.game.navigation.gameScreen
import ui.game.navigation.navigateToGame
import ui.navigation.Screen
import ui.result.navigation.navigateToResult
import ui.result.navigation.resultScreen
import ui.theme.WouldYouRatherGameTheme


val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun App(
    navController: NavHostController = rememberNavController(),
) {
    KoinApplication(application = koinAppDeclaration) {
        WouldYouRatherGameTheme {
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
                            gameScreen(
                                onShowResult = {
                                    navController.navigateToResult(it)
                                }
                            )
                            resultScreen(
                                onRestart = {
                                    navController.navigateToGame()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
