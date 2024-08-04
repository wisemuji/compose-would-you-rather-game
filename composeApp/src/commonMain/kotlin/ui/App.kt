package ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import compose_would_you_rather_game.composeapp.generated.resources.Res
import compose_would_you_rather_game.composeapp.generated.resources.loading
import di.networkModule
import di.repositoryModule
import di.serviceModule
import di.viewModelModule
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.getKoin

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(
            serviceModule,
            networkModule,
            repositoryModule,
            viewModelModule,
        )
    }) {
        val viewModel: MainViewModel = getKoin().get()
        val uiState: GameUiState by viewModel.uiState.collectAsState()

        LaunchedEffect(key1 = Unit) {
            viewModel.startGame()
        }

        MaterialTheme {
            Box(modifier = Modifier.fillMaxSize()) {
                when (val us = uiState) {
                    is GameUiState.SelectableOptions -> {
                        SelectableOptionsScreen(
                            uiState = us,
                            onOptionSelected = { option ->
                                viewModel.selectOption(option)
                            }
                        )
                    }

                    is GameUiState.GameOver -> {
                        GameOverScreen(
                            uiState = us,
                            onRestartClick = {
                                viewModel.startGame()
                            }
                        )
                    }

                    GameUiState.Loading -> Text(stringResource(Res.string.loading))
                }
            }
        }
    }
}

