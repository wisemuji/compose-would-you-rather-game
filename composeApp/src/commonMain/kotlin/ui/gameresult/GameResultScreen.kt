package ui.gameresult

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose_would_you_rather_game.composeapp.generated.resources.Res
import compose_would_you_rather_game.composeapp.generated.resources.result_title
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ui.LocalNavAnimatedVisibilityScope
import ui.LocalSharedTransitionScope
import ui.gameresult.component.RestartColumn

private const val CONFETTI_LOTTIE_FILE = "files/confetti.json"
private const val CONFETTI_DURATION = 3_000L


@Composable
internal fun ResultScreen(
    gameResult: GameResult,
    navigateToGame: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ResultViewModel = koinViewModel { parametersOf(gameResult) },
) {
    val uiState: GameResultUiState by viewModel.uiState.collectAsState()
    val restart: Boolean by viewModel.restart.collectAsState()

    LaunchedEffect(restart) {
        if (restart) {
            navigateToGame()
        }
    }

    ResultScreen(
        uiState = uiState,
        onRestartClick = { viewModel.restart() },
        modifier = modifier
    )
}

@Composable
private fun ResultScreen(
    uiState: GameResultUiState,
    onRestartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showConfetti by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(CONFETTI_DURATION)
        showConfetti = false
    }
    Box {
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
                    text = stringResource(Res.string.result_title),
                    fontSize = 34.sp,
                    fontWeight = Bold,
                    color = Color.White,
                    letterSpacing = 0.1.sp,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.padding(12.dp))
                FinalChoiceBox(
                    text = uiState.gameResult.optionComment,
                    modifier = Modifier
                        .fillMaxSize()
                )
                Text(
                    text = uiState.gameResult.lesson,
                    fontSize = 16.sp,
                    color = Color.Black,
                    letterSpacing = 0.25.sp,
                    lineHeight = 22.sp,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            RestartColumn(
                onRestartClick = onRestartClick,
                modifier = Modifier.padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 60.dp,
                    top = 20.dp
                ),
            )
        }
        AnimatedVisibility(
            visible = showConfetti,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Confetti(modifier = Modifier.fillMaxSize())
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun FinalChoiceBox(
    text: String,
    modifier: Modifier = Modifier,
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No SharedElementScope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No AnimatedVisibility found")
    Box(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        with(sharedTransitionScope) {
            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = Medium,
                textAlign = TextAlign.Center,
                color = Color.Black,
                letterSpacing = 0.1.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .sharedElement(
                        rememberSharedContentState(key = text),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun Confetti(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes(CONFETTI_LOTTIE_FILE).decodeToString()
        )
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = rememberLottiePainter(
                composition = composition,
                iterations = 1,
            ),
            contentDescription = "Confetti animation"
        )
    }
}
