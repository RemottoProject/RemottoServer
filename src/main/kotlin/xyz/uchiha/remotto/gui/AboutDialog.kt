package xyz.uchiha.remotto.gui

import xyz.uchiha.remotto.gui.ImageAssets.APP_ICON
import xyz.uchiha.remotto.gui.ImageAssets.APP_ICON_16
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import javax.swing.*
import javax.swing.border.EmptyBorder

class AboutDialog(val frame: JFrame) : JDialog(frame, true) {
    init {
        setIconImage(ImageAssets.getImage(APP_ICON_16))
        title = "About"
        isResizable = false

        val panel = JPanel(FlowLayout(FlowLayout.LEFT, 5, 5))
        val left = JPanel()
        val right = JPanel()
        right.layout = BoxLayout(right, BoxLayout.PAGE_AXIS)

        val icon = JLabel(ImageAssets.getImageIcon(APP_ICON))
        val title = StyledLabel("Remotto Server", Font.BOLD, 18, Color(50, 50, 50))
        val ver = StyledLabel("Alpha 1", Font.ITALIC, pb = 8)
        val desc = StyledLabel("<html><p style=\\\"width:300px\\\">Control your computer remotely with your smartphone.</p></html>", pb = 4)
        val license = StyledLabel("Distributed under Apache 2.0 license.", pb = 8)
        val libsTitle = StyledLabel("This software exists thanks to the inclusion of the following libraries:", pt = 8, pb = 4)
        val qrcode = StyledLabel("- QR Code generator", Font.BOLD, color = Color(40, 40, 40))
        val jnativehook = StyledLabel("- JNativeHook", Font.BOLD, color = Color(40, 40, 40), pb = 4)
        val libsLicense = StyledLabel("The libraries are distributed under their own licenses.")

        left.add(icon)
        right.add(title)
        right.add(ver)
        right.add(desc)

        val warnText = StyledLabel("<html><p style=\\\"width:300px\\\">This is an early access build.<br>Visit <a href=\\\"\\\">https://remotto.uchiha.xyz</a> for the latest version.</p></html>", pb = 8)
        addHyperlink(warnText, "https://remotto.uchiha.xyz")
        right.add(StyledLabel("Warning", Font.BOLD, 14, Color(40, 40, 40), pt = 4, pb = 4))
        right.add(warnText)

        right.add(license)
        right.add(JSeparator())
        right.add(libsTitle)
        right.add(qrcode)
        right.add(jnativehook)
        right.add(libsLicense)
        right.border = EmptyBorder(8, 8, 16, 12)

        panel.add(left)
        panel.add(right)

        contentPane = panel
        pack()

        setupComponents()
    }

    private fun setupComponents() {
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(we: WindowEvent) {
                hideDialog()
            }
        })
    }

    fun showDialog() {
        setLocationRelativeTo(frame)
        isVisible = true
    }

    fun hideDialog() {
        isVisible = false
    }

    private fun addHyperlink(label: JLabel, url: String) {
        label.cursor = Cursor(Cursor.HAND_CURSOR)
        label.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                try {
                    Desktop.getDesktop().browse(URI(url))
                } catch (ex: URISyntaxException) {
                    //It looks like there's a problem
                } catch (ex: IOException) {
                }
            }
        })
    }
}

private class StyledLabel(
    text: String,
    style: Int = Font.PLAIN,
    size: Int = 12,
    color: Color = Color.BLACK,
    pt: Int = 0, pr: Int = 0, pb: Int = 0, pl: Int = 0) : JLabel(text){
    init {
        font = Font(font.name, style, size)
        foreground = color
        border = EmptyBorder(pt, pl, pb, pr)
    }
}