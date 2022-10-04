package xyz.uchiha.remotto.gui

import java.awt.Component
import javax.swing.*
import javax.swing.border.EmptyBorder
import xyz.uchiha.remotto.gui.ImageAssets.*

class ClientListItemRenderer : JLabel(), ListCellRenderer<Client> {
    private val PADDING = 4

    override fun getListCellRendererComponent(list: JList<out Client>, value: Client, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component {
        if (isSelected) {
            isOpaque = true
            foreground = list.selectionForeground
            background = list.selectionBackground
        } else {
            isOpaque = false
            foreground = list.foreground
            background = list.background
        }

        // val f = JLabel("").font
        /*font = if (isSelected) {
            f.deriveFont(f.style or Font.BOLD)
        } else {
            f.deriveFont(f.style or Font.PLAIN)
        }*/

        val img = when(value.isEnabled) {
            true -> ImageIcon(ImageAssets.getResource(CHECK_CIRCLE.fileName))
            else -> ImageIcon(ImageAssets.getResource(CLOSE_CIRCLE.fileName))
        }

        border = EmptyBorder(PADDING, PADDING, PADDING, PADDING)
        text = value.displayName
        icon = img
        toolTipText = if (value.isEnabled) "Remote control enabled" else "Remote control disabled"

        return this
    }
}