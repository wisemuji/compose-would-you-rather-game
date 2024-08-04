package di

import org.koin.dsl.module
import ui.MainViewModel

val viewModelModule = module {
    single { MainViewModel(get()) }
}
