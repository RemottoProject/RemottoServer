package xyz.uchiha.remotto.bg

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import java.awt.MouseInfo
import java.awt.Robot
import java.awt.Toolkit
import java.awt.datatransfer.*
import java.awt.event.InputEvent
import java.awt.event.KeyEvent.*

class Actions {
    companion object {
        val robot = Robot()
        val MOUSE_LEFT = InputEvent.BUTTON1_DOWN_MASK
        val MOUSE_RIGHT = InputEvent.BUTTON3_DOWN_MASK

        fun mediaKeyStop() {
            println("mediaKeyStop: ")

            GlobalScreen.postNativeEvent(
                NativeKeyEvent(
                    2401,
                    0,
                    RawCodes.stop,
                    NativeKeyEvent.VC_MEDIA_STOP,
                    NativeKeyEvent.CHAR_UNDEFINED
                )
            )
        }

        fun mediaKeyVolDown() {
            println("mediaKeyVolDown: ")

            GlobalScreen.postNativeEvent(
                NativeKeyEvent(
                    2401,
                    0,
                    RawCodes.volDown,
                    NativeKeyEvent.VC_VOLUME_DOWN,
                    NativeKeyEvent.CHAR_UNDEFINED
                )
            )
        }

        fun mediaKeyVolUp() {
            println("mediaKeyVolUp: ")

            GlobalScreen.postNativeEvent(
                NativeKeyEvent(
                    2401,
                    0,
                    RawCodes.volUp,
                    NativeKeyEvent.VC_VOLUME_UP,
                    NativeKeyEvent.CHAR_UNDEFINED
                )
            )
        }

        fun mediaKeyMute() {
            println("mediaKeyMute: ")

            GlobalScreen.postNativeEvent(
                NativeKeyEvent(
                    2401,
                    0,
                    RawCodes.mute,
                    NativeKeyEvent.VC_VOLUME_MUTE,
                    NativeKeyEvent.CHAR_UNDEFINED
                )
            )
        }

        fun mediaKeyNext() {
            println("mediaKeyNext: ")

            GlobalScreen.postNativeEvent(
                NativeKeyEvent(
                    2401,
                    0,
                    RawCodes.next,
                    57369,
                    NativeKeyEvent.CHAR_UNDEFINED
                )
            )
        }

        fun mediaKeyPrevious() {
            println("mediaKeyPrevious: ")

            GlobalScreen.postNativeEvent(
                NativeKeyEvent(
                    2401,
                    0,
                    RawCodes.prev,
                    57360,
                    NativeKeyEvent.CHAR_UNDEFINED
                )
            )
        }

        fun mediaKeyPlay() {
            println("mediaKeyPlay: ")

            GlobalScreen.postNativeEvent(
                NativeKeyEvent(
                    2401,
                    0,
                    RawCodes.play,
                    57378,
                    NativeKeyEvent.CHAR_UNDEFINED
                )
            )
        }

        fun keyUp() {
            robot.keyPress(VK_UP)
            robot.keyRelease(VK_UP)
        }

        fun keyRight() {
            robot.keyPress(VK_RIGHT)
            robot.keyRelease(VK_RIGHT)
        }

        fun keyDown() {
            robot.keyPress(VK_DOWN)
            robot.keyRelease(VK_DOWN)
        }

        fun keyLeft() {
            robot.keyPress(VK_LEFT)
            robot.keyRelease(VK_LEFT)
        }

        fun keyEnter() {
            robot.keyPress(VK_ENTER)
            robot.keyRelease(VK_ENTER)
        }

        fun keyBack() {
            robot.keyPress(VK_BACK_SPACE)
            robot.keyRelease(VK_BACK_SPACE)
        }

        fun writeChars(text: String, pressReturn: Boolean = false) {
            for (c in text) {
                val ci = c.toUpperCase().toInt()
                robot.keyPress(ci)
                robot.keyRelease(ci)
            }

            if (pressReturn) {
                robot.keyPress(VK_ENTER)
                robot.keyRelease(VK_ENTER)
            }
        }

        /**
         * Paste text in current focused window.
         * @param text Text to be writen
         * @param pressReturn True for automatic press enter after text paste.
         */
        fun pasteText(text: String, pressReturn: Boolean = false){
            // create copy of clipboard content
            val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
            val copy = clipboard.getContents(null)

            // Copy text to clipboard
            val selection = StringSelection(text)
            clipboard.setContents(selection, null)

            // Paste it
            robot.keyPress(VK_CONTROL)
            robot.keyPress(VK_V)
            robot.keyRelease(VK_V)
            robot.keyRelease(VK_CONTROL)

            if (pressReturn) {
                robot.keyPress(VK_ENTER)
                robot.keyRelease(VK_ENTER)
            }

            Thread.sleep(50)   // wait for the robot to finish pressing the keys

            // Put the original clipboard content back.
            // This may throw IOException, ClassNotFoundException and others as debug messages
            // depending on the JDK version you're using.
            clipboard.setContents(copy, null)
        }

        fun zoomIn() {
            println("zoomIn: ")
            robot.keyPress(VK_CONTROL)
            robot.keyPress(VK_ADD)
            robot.keyRelease(VK_ADD)
            robot.keyRelease(VK_CONTROL)
        }

        fun zoomOut() {
            println("zoomOut: ")
            robot.keyPress(VK_CONTROL)
            robot.keyPress(VK_MINUS)
            robot.keyRelease(VK_MINUS)
            robot.keyRelease(VK_CONTROL)
        }

        fun copy() {
            println("copy: ")
            robot.keyPress(VK_CONTROL)
            robot.keyPress(VK_C)
            robot.keyRelease(VK_C)
            robot.keyRelease(VK_CONTROL)
        }

        fun paste() {
            println("paste: ")
            robot.keyPress(VK_CONTROL)
            robot.keyPress(VK_V)
            robot.keyRelease(VK_V)
            robot.keyRelease(VK_CONTROL)
        }

        fun mouseLeftClick() {
            println("mouseLeftClick: ")
            robot.mousePress(MOUSE_LEFT)
            robot.mouseRelease(MOUSE_LEFT)
        }

        fun mouseRightClick() {
            println("mouseRightClick: ")
            robot.mousePress(MOUSE_RIGHT)
            robot.mouseRelease(MOUSE_RIGHT)
        }

        fun mouseLDoubleClick() {
            println("mouseLDoubleClick: ")
            robot.mousePress(MOUSE_LEFT)
            robot.mouseRelease(MOUSE_LEFT)
            robot.mousePress(MOUSE_LEFT)
            robot.mouseRelease(MOUSE_LEFT)
        }

        fun mouseMove(data: String) {
            // println("mouseMove: ") // hidden for performance

            val x = data.substring(0, 4).toInt()
            val y = data.substring(4, 8).toInt()
            val sequence = data.substring(8, 20).toInt()

            val mouseLocation = MouseInfo.getPointerInfo().location
            val newX = mouseLocation.x + x
            val newY = mouseLocation.y + y

            robot.mouseMove(newX, newY)
        }

        fun mouseScrollVer(data: String) {
            // println("mouseScrollVer: ") // hidden for performance

            val y = data.substring(0, 4).toInt()
            val sequence = data.substring(4, 16).toInt()

            println("*** Scroll: $y --- sequence: $sequence") // debug
            robot.mouseWheel(y)
        }

        fun mouseScrollHor(data: String) {
            println("mouseScrollHor: ")

        }

        fun mouseLeftHold(){
            println("mouseLeftHold: ")
            robot.mousePress(MOUSE_LEFT)
        }

        fun mouseLeftRelease(){
            println("mouseLeftRelease: ")
            robot.mouseRelease(MOUSE_LEFT)
        }

        // TODO: fazer variação dos metodos para sistemas unix
        fun hibernate(){
            val runtime = Runtime.getRuntime()
            runtime.exec("powershell.exe -C \"\$m='[DllImport(\\\"Powrprof.dll\\\",SetLastError=true)]static extern bool SetSuspendState(bool hibernate,bool forceCritical,bool disableWakeEvent);public static void PowerSleep(){SetSuspendState(false,false,false); }';add-type -name Import -member \$m -namespace Dll; [Dll.Import]::PowerSleep();\"")
        }

        fun shutdown(){
            val runtime = Runtime.getRuntime()
            runtime.exec("shutdown -s -t 00")
        }
    }
}