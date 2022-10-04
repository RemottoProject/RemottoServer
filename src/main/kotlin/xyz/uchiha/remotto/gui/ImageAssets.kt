package xyz.uchiha.remotto.gui

import java.awt.Image
import java.net.URL
import javax.imageio.ImageIO
import javax.swing.ImageIcon

enum class ImageAssets(val fileName: String) {
    APP_ICON("remotto_icon_256.png"),
    APP_ICON_48("remotto_icon_48.png"),
    APP_ICON_32("remotto_icon_32.png"),
    APP_ICON_16("remotto_icon_16_v2.png"),
    TRAY_ICON_16("remotto_tray_icon_win10.png"),
    CHECK_CIRCLE("check_circle_16.png"),
    CLOSE_CIRCLE("cancel_circle_16.png"),
    ENABLE("check_16.png"),
    DISABLE("close_16.png"),
    PAUSE("pause_16.png"),
    PLAY("play_16.png"),
    EMPTY("empty_icon_16.png");

    companion object {
        /**
         * Loads and return a image icon.
         */
        fun getImageIcon(asset: ImageAssets): ImageIcon {
            val r = getImage(asset)
            return ImageIcon(r)
        }

        /**
         * Loads and return a image.
         */
        fun getImage(asset: ImageAssets): Image {
            val r = getResource(asset.fileName)
            return ImageIO.read(r)
        }

        /**
         * Loads a resource from resources/images folder.
         * @param fileName File name including extension.
         */
        fun getResource(fileName: String): URL? {
            return this.javaClass.getResource("/images/$fileName")
        }
    }
}