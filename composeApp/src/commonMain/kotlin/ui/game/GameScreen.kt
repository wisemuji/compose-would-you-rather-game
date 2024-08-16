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

package ui.game

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import model.GameResult
import model.Option
import org.koin.compose.viewmodel.koinViewModel
import ui.LocalNavAnimatedVisibilityScope
import ui.LocalSharedTransitionScope
import ui.component.LottieImage
import ui.loading.LoadingScreen
import ui.theme.Blue40
import ui.theme.Blue50
import ui.theme.Blue60
import ui.theme.Blue70
import ui.theme.Pink20
import ui.theme.Pink40
import ui.theme.Pink50
import ui.theme.Pink60

private const val GHOST_LOTTIE_FILE = "files/ghost.json"

@Composable
internal fun GameScreen(
    onShowResult: (GameResult) -> Unit,
    onError: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = koinViewModel(),
) {
    val uiState: GameUiState by viewModel.uiState.collectAsState()
    val showResult by viewModel.showResult.collectAsState()
    val showError by viewModel.showError.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startGame()
    }
    LaunchedEffect(showResult) {
        showResult?.let { onShowResult(it) }
    }
    LaunchedEffect(showError) {
        if (showError) onError()
    }

    when (val uiState = uiState) {
        GameUiState.LoadingGame -> {
            LoadingScreen()
        }

        is GameUiState.SelectableOptions -> {
            GameScreen(
                uiState = uiState,
                onOptionSelected = { viewModel.selectOption(it) },
                modifier = modifier
            )
        }
    }
}

@Composable
private fun GameScreen(
    uiState: GameUiState.SelectableOptions,
    onOptionSelected: (Option) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box {
        Column(
            modifier = modifier
                .background(color = MaterialTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OptionButton(
                text = uiState.optionA,
                option = Option.A,
                onClick = { onOptionSelected(it) },
                enabled = !uiState.isLoadingOptions,
                modifier = Modifier.weight(1f),
            )
            OptionButton(
                text = uiState.optionB,
                option = Option.B,
                onClick = { onOptionSelected(it) },
                enabled = !uiState.isLoadingOptions,
                modifier = Modifier.weight(1f),
            )
        }
        QuestionBox(
            text = uiState.question,
            modifier = Modifier.align(Alignment.Center)
        )
        if (uiState.isLoadingOptions) {
            LottieImage(
                jsonString = GHOST_LOTTIE_FILE,
                iterations = 1,
                contentDescription = "Ghost animation",
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun QuestionBox(
    text: String,
    modifier: Modifier = Modifier,
) {
    val animationValue by rememberInfiniteTransition().animateBreathing()

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = animationValue
                scaleY = animationValue
            }
            .padding(24.dp)
            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
            .background(
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .padding(12.dp)
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun OptionButton(
    text: String,
    option: Option,
    onClick: (Option) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No SharedElementScope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No AnimatedVisibility found")

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = when (option) {
                        Option.A -> listOf(Pink60, Pink50, Pink40, Pink20)
                        Option.B -> listOf(Blue40, Blue50, Blue60, Blue70)
                    },
                    start = Offset.Zero,
                    end = Offset.Infinite,
                ),
            )
            .clickable(enabled = enabled) { onClick(option) }
            .padding(16.dp)
    ) {
        with(sharedTransitionScope) {
            Text(
                text = text,
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primaryVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.sharedElement(
                    rememberSharedContentState(key = text),
                    animatedVisibilityScope = animatedVisibilityScope
                )
            )
        }
    }
}

@Composable
private fun InfiniteTransition.animateBreathing() = animateFloat(
    initialValue = 0.8f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
        animation = tween(
            durationMillis = 600,
            easing = LinearEasing
        ),
        repeatMode = RepeatMode.Reverse
    )
)
