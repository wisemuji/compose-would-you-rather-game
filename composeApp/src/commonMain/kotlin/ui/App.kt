/*
 * Copyright 2024 Suhyeon Kim(wisemuji), Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import compose_would_you_rather_game.composeapp.generated.resources.Res
import compose_would_you_rather_game.composeapp.generated.resources.error_loading_game
import di.koinAppDeclaration
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
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
            val snackbarHostState = remember { SnackbarHostState() }
            val snackbarMessage = stringResource(Res.string.error_loading_game)
            val coroutineScope = rememberCoroutineScope()

            Scaffold(
                scaffoldState = rememberScaffoldState(
                    snackbarHostState = snackbarHostState
                )
            ) { innerPadding ->
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
                                },
                                onError = {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(snackbarMessage)
                                    }
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
