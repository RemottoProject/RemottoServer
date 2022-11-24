package xyz.uchiha.remotto

import kotlinx.coroutines.DelicateCoroutinesApi
import xyz.uchiha.remotto.gui.MainWindow
import xyz.uchiha.remotto.gui.ReadableStrings
import java.util.*
import javax.swing.SwingUtilities

@DelicateCoroutinesApi
fun main() {
    SwingUtilities.invokeLater {
        val lang = getSystemDefaultLanguage()
        ReadableStrings.lang = lang

        val app = MainWindow("Remotto Server")
        app.startServer()
        app.isVisible = true  // only for development
    }
}

fun getSystemDefaultLanguage(): ReadableStrings.Languages {
    val lang = ReadableStrings.Languages.fromString(Locale.getDefault().toString())
    return lang ?: ReadableStrings.lang
}