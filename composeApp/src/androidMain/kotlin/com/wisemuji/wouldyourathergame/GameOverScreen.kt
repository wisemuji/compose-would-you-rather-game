package com.wisemuji.wouldyourathergame

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import compose_would_you_rather_game.composeapp.generated.resources.Res
import compose_would_you_rather_game.composeapp.generated.resources.restart
import org.jetbrains.compose.resources.stringResource
import ui.GameUiState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GameOverScreen(
    uiState: GameUiState.GameOver,
    onRestartClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        with(sharedTransitionScope) {
            Text(
                text = uiState.selectedOption.comment,
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = uiState.selectedOption.option.name),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
            )
        }
    }
    Text(text = uiState.lesson)
    TextButton(onClick = onRestartClick) {
        Text(stringResource(Res.string.restart))
    }
}
