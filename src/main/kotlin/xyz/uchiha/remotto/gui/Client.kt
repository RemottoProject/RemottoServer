package xyz.uchiha.remotto.gui

class Client(var name: String, var ip: String, var isEnabled: Boolean) {
    var displayName = "${name.replace("&", "")}  —  $ip"
}