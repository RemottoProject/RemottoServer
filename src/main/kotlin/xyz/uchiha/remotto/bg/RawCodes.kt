package xyz.uchiha.remotto.bg

import xyz.uchiha.remotto.Utils

class RawCodes {
    // Linux map
    // play      keycode: 0     - rawcode: 65300
    // stop      keycode: 0     - rawcode: 65301
    // prev      keycode: 0     - rawcode: 65302
    // next      keycode: 0     - rawcode: 65303
    // vol down  keycode: 57390 - rawcode: 65297
    // vol up    keycode: 57392 - rawcode: 65299
    // mute      keycode: 57376 - rawcode: 65298
    companion object {
        val play: Int
        val stop: Int
        val prev: Int
        val next: Int
        val volDown: Int
        val volUp: Int
        val mute: Int

        init {
            if (Utils.OS_NAME == "Linux"){
                play = 65300
                stop = 65301
                prev = 65302
                next = 65303
                volDown = 65297
                volUp = 65299
                mute = 65298
            }
            else{
                play = 179
                stop = 178
                prev = 177
                next = 176
                volDown = 174
                volUp = 175
                mute = 173
            }
        }
    }
}