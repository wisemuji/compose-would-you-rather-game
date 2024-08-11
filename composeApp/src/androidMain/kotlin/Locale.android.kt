import java.util.Locale as JavaLocale

class AndroidLocale : Locale {
    override val language: Language
        get() = JavaLocale.getDefault().language
            .let {
                when (it) {
                    "ko" -> Language.KOREAN
                    else -> Language.ENGLISH
                }
            }
}

actual fun getLocale(): Locale = AndroidLocale()
