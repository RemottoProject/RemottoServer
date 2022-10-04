package xyz.uchiha.remotto.gui

import com.github.weisj.darklaf.components.border.DarkBorders
import com.github.weisj.darklaf.ui.button.DarkButtonUI
import xyz.uchiha.remotto.gui.ImageAssets.*
import java.awt.FlowLayout
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JToolBar

class Toolbar : JPanel(FlowLayout(FlowLayout.LEFT,0,0)) {
    private val toolBar = JToolBar()
    val playBtn: JButton
    val pauseBtn: JButton
    val enableBtn: JButton
    val disableBtn: JButton

    init {
        playBtn = createItem(PLAY, "Run server")
        pauseBtn = createItem(PAUSE, "Pause server")
        enableBtn = createItem(ENABLE, "Enable device")
        disableBtn = createItem(DISABLE, "Disable device")

        toolBar.isFloatable = false
        toolBar.add(playBtn)
        toolBar.add(pauseBtn)
        toolBar.addSeparator()
        toolBar.add(enableBtn)
        toolBar.add(disableBtn)

        add(toolBar, FlowLayout.LEFT)
        border = DarkBorders.createLineBorder(0,0,1,0)
    }

    private fun createItem(iconName: ImageAssets, description: String): JButton {
        val btn = JButton()
        val icon = ImageAssets.getImage(iconName)
        btn.icon = ImageIcon(icon, description)
        btn.toolTipText = description
        btn.isFocusPainted = false
        btn.isFocusable = false
        btn.isRolloverEnabled = true
        btn.putClientProperty(DarkButtonUI.KEY_VARIANT, DarkButtonUI.VARIANT_BORDERLESS)
        btn.putClientProperty(DarkButtonUI.KEY_THIN, true)

        return btn
    }
}