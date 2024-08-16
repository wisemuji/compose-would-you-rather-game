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

package ui.game.navigation

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ui.LocalNavAnimatedVisibilityScope
import ui.game.GameScreen
import ui.navigation.Screen
import ui.navigation.URLEncodedNavType.Companion.TYPE_MAP
import model.GameResult

fun NavController.navigateToGame() {
    popBackStack()
    navigate(Screen.Game)
}

fun NavGraphBuilder.gameScreen(
    onShowResult: (GameResult) -> Unit,
    onError: () -> Unit,
) {
    composable<Screen.Game>(
        typeMap = TYPE_MAP
    ) {
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            GameScreen(
                onShowResult = onShowResult,
                onError = onError,
            )
        }
    }
}
