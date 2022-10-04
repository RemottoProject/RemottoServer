package xyz.uchiha.remotto.gui

import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.*

class ClientPanel(var ip: String, var isActive: Boolean) : JPanel() {

    init {
        layout = GridLayout()
        maximumSize = Dimension(190, 25)
        preferredSize = Dimension(190, 25)
        setSize(190, 25)

        add(JLabel(ip))

        val button = when(isActive) {
            true -> JButton("Disable")
            false -> JButton("Enable")
        }
        //button.addActionListener(listener)
        add(button)
    }
}