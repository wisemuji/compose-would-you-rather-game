package ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose_would_you_rather_game.composeapp.generated.resources.Res
import compose_would_you_rather_game.composeapp.generated.resources.loading
import model.Option
import org.jetbrains.compose.resources.stringResource

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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GameScreen(
    uiState: GameUiState,
    onSelectOption: (Option) -> Unit,
    onRestartClick: () -> Unit,
) {
    SharedTransitionLayout {
        AnimatedContent(uiState) { targetState ->
            when (targetState) {
                is GameUiState.SelectableOptions -> {
                    SelectableOptionsScreen(
                        uiState = targetState,
                        onOptionSelected = onSelectOption,
                        animatedVisibilityScope = this@AnimatedContent,
                        sharedTransitionScope = this@SharedTransitionLayout
                    )
                }

                is GameUiState.GameOver -> {
                    GameOverScreen(
                        uiState = targetState,
                        onRestartClick = onRestartClick,
                        animatedVisibilityScope = this@AnimatedContent,
                        sharedTransitionScope = this@SharedTransitionLayout
                    )
                }

                GameUiState.Loading -> Text(stringResource(Res.string.loading))
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SelectableOptionsScreen(
    uiState: GameUiState.SelectableOptions,
    onOptionSelected: (Option) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    val animationValue by rememberInfiniteTransition().animateBreathing()
    Box {
        Column(
            modifier = modifier
                .background(color = Color(0xFF262628)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OptionButton(
                text = uiState.optionA,
                option = Option.A,
                onClick = { onOptionSelected(it) },
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                modifier = Modifier.weight(1f),
            )
            OptionButton(
                text = uiState.optionB,
                option = Option.B,
                onClick = { onOptionSelected(it) },
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                modifier = Modifier.weight(1f),
            )
        }
        QuestionBox(
            value = animationValue,
            uiState = uiState,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun QuestionBox(
    value: Float,
    uiState: GameUiState.SelectableOptions,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .graphicsLayer {
                scaleX = value
                scaleY = value
            }
            .padding(24.dp)
            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
            .background(
                color = Color(0xFFffffff),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(4.dp)
    ) {
        Text(
            text = uiState.question,
            fontSize = 18.sp,
            fontWeight = Medium,
            textAlign = TextAlign.Center,
            color = Color(0xFF390ca3),
            letterSpacing = 0.1.sp,
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
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = when (option) {
                        // TODO: extract color resources
                        Option.A -> listOf(
                            Color(0xFFf21379),
                            Color(0xFFf72585),
                            Color(0xFFff5ea7),
                            Color(0xFFfa96c3)
                        )

                        Option.B -> listOf(
                            Color(0xFF8FA1F3),
                            Color(0xFF6b83f2),
                            Color(0xFF4362ee),
                            Color(0xFF2b4eed)
                        )
                    },
                    start = Offset.Zero,
                    end = Offset.Infinite,
                ),
            )
            .clickable { onClick(option) }
            .padding(16.dp)
    ) {
        with(sharedTransitionScope) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = Medium,
                textAlign = TextAlign.Center,
                lineHeight = 30.sp,
                letterSpacing = -(0.3).sp,
                modifier = Modifier.sharedElement(
                    rememberSharedContentState(key = option.name),
                    animatedVisibilityScope = animatedVisibilityScope
                )
            )
        }
    }

}
