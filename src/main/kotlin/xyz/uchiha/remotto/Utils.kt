package xyz.uchiha.remotto

import io.nayuki.qrcodegen.QrCode
import xyz.uchiha.remotto.bg.NetworkInfo
import java.awt.Color
import java.awt.Toolkit
import java.awt.image.BufferedImage
import java.net.NetworkInterface
import java.util.*
import javax.swing.KeyStroke


class Utils {
    companion object {
        val OS_NAME = System.getProperty("os.name")
        val JAVA_VERSION = System.getProperty("java.version")

        fun toImage(qr: QrCode, scale: Int, border: Int): BufferedImage {
            val black = Color(20, 20, 20, 255).rgb
            val transparency = Color(255, 255, 255, 0).rgb

            return toImage(qr, scale, border, transparency, black)
        }

        /**
         * Returns a raster image depicting the specified QR Code, with
         * the specified module scale, border modules, and module colors.
         *
         * For example, scale=10 and border=4 means to pad the QR Code with 4 light border
         * modules on all four sides, and use 10&#xD7;10 pixels to represent each module.
         * @param qr the QR Code to render (not `null`)
         * @param scale the side length (measured in pixels, must be positive) of each module
         * @param border the number of border modules to add, which must be non-negative
         * @param lightColor the color to use for light modules, in 0xRRGGBB format
         * @param darkColor the color to use for dark modules, in 0xRRGGBB format
         * @return a new image representing the QR Code, with padding and scaling
         * @throws NullPointerException if the QR Code is `null`
         * @throws IllegalArgumentException if the scale or border is out of range, or if
         * {scale, border, size} cause the image dimensions to exceed Integer.MAX_VALUE
         */
        private fun toImage(qr: QrCode, scale: Int, border: Int, lightColor: Int, darkColor: Int): BufferedImage {
            Objects.requireNonNull(qr)
            require(!(scale <= 0 || border < 0)) { "Value out of range" }
            require(!(border > Int.MAX_VALUE / 2 || qr.size + border * 2L > Int.MAX_VALUE / scale)) { "Scale or border too large" }
            val result = BufferedImage(
                (qr.size + border * 2) * scale,
                (qr.size + border * 2) * scale,
                BufferedImage.TYPE_INT_ARGB
            )
            for (y in 0 until result.height) {
                for (x in 0 until result.width) {
                    val color: Boolean = qr.getModule(x / scale - border, y / scale - border)
                    result.setRGB(x, y, if (color) darkColor else lightColor)
                }
            }
            return result
        }

        fun getNetworkList(): MutableList<NetworkInfo> {
            val res = mutableListOf<NetworkInfo>()
            val networkInterfaces: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()

            while (networkInterfaces.hasMoreElements()) {
                val network: NetworkInterface = networkInterfaces.nextElement()
                val name = network.displayName
                val ip = NetworkInfo.getIpv4(network.inetAddresses)
                val mac = NetworkInfo.formatMacAddress(network.hardwareAddress)

                if (name != "" && ip != "" && mac != "")
                    res.add(NetworkInfo(name, ip, mac))
            }

            return res
        }

        /**
         * Create shortcuts with Ctrl + key.
         * @param key Any KeyEvent constant.
         * @return A new shortcut.
         */
        fun createShortcut(key: Int): KeyStroke {
            val menuShortcut: Int
            val v = JAVA_VERSION.split(".")

            // menuShortcutKeyMaskEx only exists in java >= 10
            if (v[1].toInt() <= 9 && v[0].toInt() == 1)
                menuShortcut = Toolkit.getDefaultToolkit().menuShortcutKeyMask
            else
                menuShortcut = Toolkit.getDefaultToolkit().menuShortcutKeyMaskEx

            return KeyStroke.getKeyStroke(key, menuShortcut)
        }
    }
}