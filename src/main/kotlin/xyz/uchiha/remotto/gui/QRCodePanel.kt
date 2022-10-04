package xyz.uchiha.remotto.gui

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Image
import java.awt.image.BufferedImage
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.TitledBorder
import xyz.uchiha.remotto.gui.ReadableStrings.Companion.strings

class QRCodePanel() : JPanel() {
    init {
        layout = BorderLayout(0, 0)
        maximumSize = Dimension(400, 400)
        preferredSize = Dimension(400, 400)

        border = BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(),
            strings.qrCode,
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            null,
            null
        )
    }

    constructor(img: BufferedImage?) : this() {
        if (img != null)
            setImage(img)
    }

    fun setImage(img: BufferedImage){
        if (componentCount > 0)
            remove(0)

        val picLabel = JLabel(ImageIcon(img.getScaledInstance(380, 370, Image.SCALE_FAST)))
        add(picLabel)
    }
}