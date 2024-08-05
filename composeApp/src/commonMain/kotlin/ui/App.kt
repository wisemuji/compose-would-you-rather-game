package ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import di.koinAppDeclaration
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.getKoin

@Composable
@Preview
fun App() {
    KoinApplication(application = koinAppDeclaration) {
        val viewModel: MainViewModel = getKoin().get()
        val uiState: GameUiState by viewModel.uiState.collectAsState()

        LaunchedEffect(key1 = Unit) {
            viewModel.startGame()
        }

        MaterialTheme {
            Box(modifier = Modifier.fillMaxSize()) {
                GameScreen(
                    uiState = uiState,
                    onSelectOption = { option ->
                        viewModel.selectOption(option)
                    },
                    onRestartClick = {
                        viewModel.startGame()
                    }
                )
            }
        }
    }
}
