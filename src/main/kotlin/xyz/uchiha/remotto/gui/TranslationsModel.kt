package xyz.uchiha.remotto.gui
import kotlinx.serialization.*

@Serializable
data class TranslationsModel(
    val file: String,
    val admin: String,
    val options: String,
    val exit: String,
    val startServer: String,
    val pauseServer: String,
    val enableClient: String,
    val disableClient: String,
    val edit: String,
    val help: String,
    val about: String,
    val clients: String,
    val qrCode: String,
    val remoteControlDisabled: String,
    val remoteControlEnabled: String,
    val showHideUI: String,
    val quit: String,
    val running: String,
    val paused: String,
)
