package di

import data.network.GeminiService
import data.network.DefaultGeminiService
import org.koin.dsl.module

val serviceModule = module {
    single<GeminiService> { DefaultGeminiService(get()) }
}
