package di

import data.repository.DefaultGameRepository
import data.repository.GameRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<GameRepository> { DefaultGameRepository(get()) }
}
