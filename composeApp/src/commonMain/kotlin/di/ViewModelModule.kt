package di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ui.game.GameViewModel
import ui.gameresult.ResultViewModel

val viewModelModule = module {
    viewModel { GameViewModel(get()) }
    viewModel { parameters -> ResultViewModel(gameResult = parameters.get()) }
}
