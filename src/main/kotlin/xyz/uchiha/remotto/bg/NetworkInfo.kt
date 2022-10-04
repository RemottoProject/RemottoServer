package xyz.uchiha.remotto.bg

import java.net.Inet4Address
import java.net.InetAddress
import java.util.*

class NetworkInfo(val name: String, val ip: String, val mac: String) {
    companion object {
        /**
         * Format a MAC address from ByteArray to String.
         * @param mac The MAC address.
         * @param separator Default is ':'.
         * @return The MAC address in the format 00:00:00:00:00:00.
         */
        fun formatMacAddress(mac: ByteArray?, separator: String = ":") : String{
            if (mac != null) {
                val sb = StringBuilder()

                for (i in mac.indices)
                    sb.append(String.format("%02X%s", mac[i], separator))

                sb.deleteAt(sb.lastIndex)

                return sb.toString()
            }

            return ""
        }

        /**
         * Get the first non loopbackAddress ipv4 from a InetAddress Enumeration.
         * @param addresses InetAddress Enumeration.
         * @return the first non loopbackAddress ipv4 or an empty String.
         */
        fun getIpv4(addresses: Enumeration<InetAddress>): String{
            while (addresses.hasMoreElements()) {
                val addr = addresses.nextElement()

                if (addr is Inet4Address && !addr.isLoopbackAddress())
                    return addr.hostAddress
            }

            return ""
        }
    }
}