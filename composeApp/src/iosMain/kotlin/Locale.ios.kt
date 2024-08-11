import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

class IOSLocale : Locale {
    override val language: Language
        get() = NSLocale.currentLocale.languageCode
            .let {
                when (it) {
                    "ko" -> Language.KOREAN
                    else -> Language.ENGLISH
                }
            }
}

actual fun getLocale(): Locale = IOSLocale()
