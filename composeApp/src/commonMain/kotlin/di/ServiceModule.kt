package di

import com.wisemuji.wouldyourathergame.BuildKonfig
import data.network.DefaultGeminiService
import data.network.GeminiService
import getLocale
import org.koin.dsl.module

val serviceModule = module {
    single<GeminiService> {
        val apiKey = BuildKonfig.GEMINI_API_KEY
        val language = getLocale().language
        DefaultGeminiService(get(), apiKey, language)
    }
}
