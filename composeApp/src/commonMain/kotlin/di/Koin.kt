package di

import org.koin.dsl.KoinAppDeclaration

val koinAppDeclaration: KoinAppDeclaration = {
    modules(
        serviceModule,
        networkModule,
        repositoryModule,
        viewModelModule,
    )
}
