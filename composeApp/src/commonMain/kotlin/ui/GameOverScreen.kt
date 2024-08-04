package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import compose_would_you_rather_game.composeapp.generated.resources.Res
import compose_would_you_rather_game.composeapp.generated.resources.restart
import org.jetbrains.compose.resources.stringResource

@Composable
fun GameOverScreen(
    uiState: GameUiState.GameOver,
    onRestartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(text = uiState.lesson)
        TextButton(onClick = onRestartClick) {
            Text(stringResource(Res.string.restart))
        }
    }
}
