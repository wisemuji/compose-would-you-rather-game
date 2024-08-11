package ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
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
            AnimatedContent(uiState) { targetState ->
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

                    GameUiState.Loading -> {
                        LoadingScreen()
                    }
                }
            }
        }
    }
}
