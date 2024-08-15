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

package ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightColors = Colors(
    primary = Pink50,
    primaryVariant = Color.White,
    secondary = Blue60,
    secondaryVariant = Color.White,
    background = Gray90,
    onBackground = Gray10,
    surface = Color.White,
    onSurface = Gray90,
    error = Pink60,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onError = Color.White,
    isLight = true,
)

// TODO: Add dark color scheme
private val DarkColors = LightColors.copy(isLight = false)

@Composable
fun WouldYouRatherGameTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colors = when {
        darkTheme -> DarkColors
        else -> LightColors
    }
    MaterialTheme(
        colors = colors,
        typography = WouldYouRatherGameTypography(),
        content = content,
    )
}
