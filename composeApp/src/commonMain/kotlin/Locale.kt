interface Locale {
    val language: Language
}

enum class Language {
    KOREAN,
    ENGLISH,
}

expect fun getLocale(): Locale
