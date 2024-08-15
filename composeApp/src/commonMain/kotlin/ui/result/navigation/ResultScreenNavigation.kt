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
