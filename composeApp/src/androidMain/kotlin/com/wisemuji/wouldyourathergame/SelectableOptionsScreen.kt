package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import model.Option
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SelectableOptionsScreen(
    uiState: GameUiState.SelectableOptions,
    onOptionSelected: (Option) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(text = uiState.question)
        TextButton(onClick = { onOptionSelected(Option.A) }) {
            Text(text = uiState.optionA)
        }
        TextButton(onClick = { onOptionSelected(Option.B) }) {
            Text(text = uiState.optionB)
        }
    }
}

@Preview
@Composable
fun SelectableOptionsScreenPreview(modifier: Modifier = Modifier) {
    SelectableOptionsScreen(
        uiState = GameUiState.SelectableOptions(
            question = "Question",
            optionA = "Option A",
            optionB = "Option B",
            remainingTurns = 3,
        ),
        onOptionSelected = {},
        modifier = modifier,
    )
}