package com.wisemuji.wouldyourathergame

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose_would_you_rather_game.composeapp.generated.resources.Res
import compose_would_you_rather_game.composeapp.generated.resources.restart
import model.Option
import org.jetbrains.compose.resources.stringResource
import ui.GameUiState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GameResultScreen(
    uiState: GameUiState.GameResult,
    onRestartClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(Color(0xFF303030))
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .shadow(8.dp, RoundedCornerShape(0.dp, 0.dp, 36.dp, 36.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF3A0CA3),
                            Color(0xFF4361EE),
                            Color(0xFFa9b4eb),
                        ),
                        startY = 0f,
                        endY = 1000f,
                    ),
                )
                .padding(top = 62.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
        ) {
            Text(
                text = "Your final choice is...",
                fontSize = 34.sp,
                fontWeight = Bold,
                color = Color.White,
                letterSpacing = 0.1.sp,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.padding(12.dp))
            Box(
                modifier = Modifier
                    .shadow(8.dp, RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                with(sharedTransitionScope) {
                    Text(
                        text = uiState.selectedOption.comment,
                        fontSize = 18.sp,
                        fontWeight = Medium,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        letterSpacing = 0.1.sp,
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState(key = uiState.selectedOption.option.name),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    )
                }
            }
            Text(
                text = uiState.lesson,
                fontSize = 16.sp,
                color = Color.Black,
                letterSpacing = 0.25.sp,
                lineHeight = 22.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            verticalArrangement = spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 60.dp, top = 20.dp),
        ) {
            Text(
                text = "Unhappy with the result? \uD83D\uDE44",
                fontSize = 18.sp,
                color = Color(0xFFdbdbdb),
                letterSpacing = 0.1.sp,
                lineHeight = 22.sp,
                modifier = Modifier
            )
            Button(
                onClick = onRestartClick,
                colors = ButtonDefaults
                    .buttonColors(backgroundColor = Color(0xFFF72585)),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(Res.string.restart),
                    fontSize = 18.sp,
                    fontWeight = Bold,
                    color = Color.White,
                    letterSpacing = 0.1.sp,
                    lineHeight = 22.sp,
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
private fun GameResultScreenPreview() {
    SharedTransitionLayout {
        AnimatedVisibility(visible = true) {
            GameResultScreen(
                uiState = GameUiState.GameResult(
                    selectedOption = GameUiState.GameResult.SelectedOption(
                        option = Option.A,
                        comment = "\uD83E\uDD11\n\nMake \uD83D\uDC861 billion won with an app I created, work 20 hours a day to maintain it, and deal with angry users' comments every day",
                    ),
                    lesson = "You chose to make 1 billion won! \uD83D\uDD25 Dealing with angry users and working 20 hours a day... you're truly dedicated! \uD83D\uDC4F\nThis balance game made us realize! ✨\nThere's no right or wrong answer in life! \uD83D\uDCAF  The important thing is to find what you truly \uD83D\uDC96want\uD83D\uDC96 and have the \uD83D\uDCAA\uD83D\uDD25 courage and passion\uD83D\uDD25\uD83D\uDCAA to see it through!  \nAll you developers, believe in yourselves and chase your dreams! \uD83D\uDE80 You are the ones who will change the world! \uD83D\uDE4C\uD83C\uDF89",
                ),
                onRestartClick = {},
                sharedTransitionScope = this@SharedTransitionLayout,
                animatedVisibilityScope = this@AnimatedVisibility,
            )
        }
    }
}
