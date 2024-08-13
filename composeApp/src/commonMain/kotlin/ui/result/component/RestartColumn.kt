package ui.result.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose_would_you_rather_game.composeapp.generated.resources.Res
import compose_would_you_rather_game.composeapp.generated.resources.restart
import compose_would_you_rather_game.composeapp.generated.resources.restart_caption
import org.jetbrains.compose.resources.stringResource

@Composable
fun RestartColumn(
    onRestartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Absolute.spacedBy(8.dp),
        modifier = modifier,
    ) {
        Text(
            text = stringResource(Res.string.restart_caption),
            fontSize = 18.sp,
            color = Color(0xFFdbdbdb),
            letterSpacing = 0.1.sp,
            lineHeight = 22.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        RestartButton(onRestartClick)
    }
}

@Composable
private fun RestartButton(
    onRestartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onRestartClick,
        colors = ButtonDefaults
            .buttonColors(backgroundColor = Color(0xFFF72585)),
        modifier = modifier
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
