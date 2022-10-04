package xyz.uchiha.remotto.bg

import java.net.DatagramPacket

class Packet(datagram: DatagramPacket) : IPacket {
    private val pkgData = String(datagram.data, 0, datagram.length, Charsets.UTF_8)
    val action: MessageCodes
    val data: String
    val sender: String
    val ip: String

    init {
        val ac = pkgData.substring(0, 3)
        action = MessageCodes.fromString(ac)!!
        sender = pkgData.substring(3, 12)
        data = pkgData.removeRange(0, 12)
        ip = datagram.address.hostAddress
    }
}