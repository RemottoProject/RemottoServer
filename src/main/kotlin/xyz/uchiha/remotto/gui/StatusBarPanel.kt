package xyz.uchiha.remotto.gui

import java.awt.FlowLayout
import java.awt.Font
import javax.swing.JLabel
import javax.swing.JPanel

class StatusBarPanel(var ip: String, var mac: String) : JPanel(FlowLayout(FlowLayout.CENTER,5,7)) {
    private val ipLabel = JLabel("IP:")
    private val macLabel = JLabel("MAC:")
    private val ipTextLabel = JLabel(ip)
    private val macTextLabel = JLabel(mac)

    constructor() : this("", "")

    init {
        val f = JLabel("").font
        ipLabel.font = f.deriveFont(f.style or Font.BOLD)
        macLabel.font = f.deriveFont(f.style or Font.BOLD)

        add(ipLabel)
        add(ipTextLabel)
        add(JLabel("—"))
        add(macLabel)
        add(macTextLabel)
    }

    fun setIP(ip: String){
        this.ip = ip
        ipTextLabel.text = ip
    }

    fun setMAC(mac: String){
        this.mac = mac
        macTextLabel.text = mac
    }
}