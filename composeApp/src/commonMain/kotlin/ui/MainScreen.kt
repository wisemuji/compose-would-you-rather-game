package ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import model.Option
import ui.game.GameScreen
import ui.gameresult.GameResultScreen
import ui.loading.LoadingScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainScreen(
    uiState: GameUiState,
    onSelectOption: (Option) -> Unit,
    onRestartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        SharedTransitionLayout {
            AnimatedContent(
                targetState = uiState,
                transitionSpec = {
                    fadeIn(animationSpec = tween(0, delayMillis = 0))
                        .togetherWith(fadeOut(animationSpec = tween(0)))
                }
            ) { targetState ->
                when (targetState) {
                    is GameUiState.SelectableOptions -> {
                        GameScreen(
                            uiState = targetState,
                            onOptionSelected = onSelectOption,
                            animatedVisibilityScope = this@AnimatedContent,
                            sharedTransitionScope = this@SharedTransitionLayout
                        )
                    }

                    is GameUiState.GameResult -> {
                        GameResultScreen(
                            uiState = targetState,
                            onRestartClick = onRestartClick,
                            animatedVisibilityScope = this@AnimatedContent,
                            sharedTransitionScope = this@SharedTransitionLayout
                        )
                    }

                    GameUiState.LoadingGame -> {
                        LoadingScreen()
                    }
                }
            }
        }
    }
}
