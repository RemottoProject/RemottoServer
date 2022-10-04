package xyz.uchiha.remotto.gui

import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class ReadableStrings {
    companion object {
        var lang: Languages = Languages.EN
            set(value) {
                field = value
                strings = Json.decodeFromString(readTranslation(lang))
            }

        var strings: TranslationsModel = Json.decodeFromString(readTranslation(lang))
            private set

        private fun readTranslation(lang: Languages): String {
            val res = this.javaClass.getResource("/strings/translations/${lang.value}.json")?.readText()

            return res ?: ""
        }
    }

    enum class Languages(val value: String) {
        EN("en"), PT_BR("pt_BR");

        companion object {
            private val map = values().associateBy(Languages::value)
            fun fromString(string: String) = map[string]
        }
    }
}