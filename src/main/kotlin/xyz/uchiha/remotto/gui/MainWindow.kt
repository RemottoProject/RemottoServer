package xyz.uchiha.remotto.gui

import com.github.weisj.darklaf.LafManager
import com.github.weisj.darklaf.settings.ThemeSettings
import io.nayuki.qrcodegen.QrCode
import kotlinx.coroutines.*
import xyz.uchiha.remotto.Utils
import xyz.uchiha.remotto.gui.ImageAssets.*
import xyz.uchiha.remotto.Utils.Companion.getNetworkList
import xyz.uchiha.remotto.Utils.Companion.toImage
import xyz.uchiha.remotto.bg.Actions
import xyz.uchiha.remotto.bg.MessageCodes
import xyz.uchiha.remotto.bg.Packet
import xyz.uchiha.remotto.gui.ReadableStrings.Companion.strings
import java.awt.*
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketException
import javax.swing.*

@DelicateCoroutinesApi
class MainWindow(var windowTitle: String, private val port: Int = 10325) : JFrame() {
    private val ip: String
    private val mac: String
    private val bufferSize = 256
    private var ds = DatagramSocket(port)
    private val data = ByteArray(bufferSize)    // buffer for data
    private val pkg = DatagramPacket(data, data.size)

    private var packetsCount = 0
    private var isRunning = true
        set(value) {
            field = value

            statusText = if (value) strings.running else strings.paused
            title = "$windowTitle - $statusText"
        }

    private var statusText: String

    private val clientList = mutableMapOf<String, Boolean>()

    // UI components
    private val toolbar: Toolbar
    private val clientListPanel: ClientListPanel
    private val qrCodePanel: QRCodePanel
    private val statusBar: StatusBarPanel
    private val aboutDialog: AboutDialog

    init {
        statusText = strings.running

        val networkList = getNetworkList()

        if (networkList.size > 0) {
            ip = networkList[0].ip
            mac = networkList[0].mac
        }
        else{
            ip = "Not found"
            mac = "Not found"
        }

        setLookAndFeel()
        toolbar = Toolbar()
        clientListPanel = ClientListPanel()
        qrCodePanel = QRCodePanel()
        statusBar = StatusBarPanel()

        setupMenu()
        setupUI()
        setupTrayIcon()
        setupListeners()

        aboutDialog = AboutDialog(this)
    }

    private fun setLookAndFeel(){
        LafManager.install()
        UIManager.put("MenuItem.acceleratorDelimiter", "+")
    }

    private fun setupUI() {
        title = windowTitle
        //iconImage = ImageAssets.getImageIcon(APP_ICON)
        iconImages = listOf(
            ImageAssets.getImage(APP_ICON),
            ImageAssets.getImage(APP_ICON_48),
            ImageAssets.getImage(APP_ICON_32),
            ImageAssets.getImage(APP_ICON_16)
        )
        isVisible = false    // 'false' initialize window closed
        isResizable = false
        size = Dimension(610, 610)
        defaultCloseOperation = EXIT_ON_CLOSE
        contentPane.layout = BoxLayout(contentPane, BoxLayout.PAGE_AXIS)    // set content layout
        setLocationRelativeTo(null)                                         // set window pos centered

        // ---------------------- left panel ----------------------

        // only uncomment for debug
        //clientListPanel.add(Client("Device 1", "192.168.0.12", true))
        //clientListPanel.add(Client("Device 2", "192.168.0.15", false))

        // ---------------------- right panel ----------------------

        val qr0: QrCode = QrCode.encodeText("$ip-$mac", QrCode.Ecc.MEDIUM)
        val img: BufferedImage = toImage(qr0, 4, 1) // See QrCodeGeneratorDemo

        qrCodePanel.setImage(img)

        // ---------------------- status bar panel ----------------------

        statusBar.setIP(ip)
        statusBar.setMAC(mac)

        // ---------------------- main UI panel ----------------------

        val mainPanel = JPanel()
        mainPanel.layout = FlowLayout(FlowLayout.LEFT, 2, 5)    // set content layout
        mainPanel.add(clientListPanel)
        mainPanel.add(qrCodePanel)

        add(toolbar)
        add(mainPanel)
        add(JSeparator())
        add(statusBar)

        pack()
    }

    private fun setupMenu(){
        val menuBar = JMenuBar()
        jMenuBar = menuBar

        val fileMenu = JMenu(strings.file)
        val adminMenu = JMenu(strings.admin)
        val optionsMenu = JMenu(strings.options)

        menuBar.add(fileMenu)
        menuBar.add(adminMenu)
        menuBar.add(optionsMenu)

        val startAction = JMenuItem(strings.startServer)
        val pauseAction = JMenuItem(strings.pauseServer)
        val enableAction = JMenuItem(strings.enableClient)
        val disableAction = JMenuItem(strings.disableClient)

        startAction.icon = ImageAssets.getImageIcon(PLAY)
        pauseAction.icon = ImageAssets.getImageIcon(PAUSE)
        enableAction.icon = ImageAssets.getImageIcon(ENABLE)
        disableAction.icon = ImageAssets.getImageIcon(DISABLE)

        val exitAction = JMenuItem(strings.exit)
        val editAction = JMenuItem(strings.edit)
        val helpAction = JMenuItem(strings.help)
        val aboutAction = JMenuItem(strings.about)

        exitAction.icon = ImageAssets.getImageIcon(EMPTY)
        editAction.icon = ImageAssets.getImageIcon(EMPTY)
        helpAction.icon = ImageAssets.getImageIcon(EMPTY)
        aboutAction.icon = ImageAssets.getImageIcon(EMPTY)

        startAction.isEnabled = false // server start running so this option start disabled
        startAction.addActionListener {
            startAction.isEnabled = false
            pauseAction.isEnabled = true
            startServer()
        }
        pauseAction.addActionListener {
            startAction.isEnabled = true
            pauseAction.isEnabled = false
            pauseServer()
        }
        enableAction.addActionListener { changeState(true) }
        disableAction.addActionListener { changeState(false) }

        exitAction.addActionListener {
            println("exiting")
        }
        editAction.addActionListener {
            println("edit")
        }
        helpAction.addActionListener {
            println("help")
            ThemeSettings.showSettingsDialog(parent)
        }
        aboutAction.addActionListener {
            println("about")
            //JOptionPane.showMessageDialog(this, "Not done yet", "About", JOptionPane.INFORMATION_MESSAGE)
            aboutDialog.showDialog()
        }

        fileMenu.add(exitAction)
        adminMenu.add(startAction)
        adminMenu.add(pauseAction)
        adminMenu.addSeparator()
        adminMenu.add(enableAction)
        adminMenu.add(disableAction)
        optionsMenu.add(editAction)
        optionsMenu.add(helpAction)
        optionsMenu.add(aboutAction)

        exitAction.accelerator = Utils.createShortcut(KeyEvent.VK_Q)
        startAction.accelerator = Utils.createShortcut(KeyEvent.VK_S)
        pauseAction.accelerator = Utils.createShortcut(KeyEvent.VK_P)
        enableAction.accelerator = Utils.createShortcut(KeyEvent.VK_E)
        disableAction.accelerator = Utils.createShortcut(KeyEvent.VK_D)
        editAction.accelerator = Utils.createShortcut(KeyEvent.VK_O)
    }

    private fun setupListeners(){
        toolbar.enableBtn.addActionListener { changeState(true) }
        toolbar.disableBtn.addActionListener { changeState(false) }
        toolbar.playBtn.addActionListener { startServer() }
        toolbar.pauseBtn.addActionListener { pauseServer() }
    }

    private fun setupTrayIcon (){
        val trayIcon: TrayIcon

        if (SystemTray.isSupported()) {
            val tray = SystemTray.getSystemTray()
            val image = ImageAssets.getImage(TRAY_ICON_16)    // load an image

            // create a action listener to listen for default action executed on the tray icon
            val listener = ActionListener { isVisible = !isVisible }

            val popup = PopupMenu() // create a popup menu

            // construct a TrayIcon
            trayIcon = TrayIcon(image, "Server Running", popup)
            trayIcon.isImageAutoSize = false
            trayIcon.addActionListener(listener)

            // create menu item for the default action
            val visibilityItem = MenuItem(strings.showHideUI)
            val startItem = MenuItem(strings.startServer)
            val pauseItem = MenuItem(strings.pauseServer)
            val quitItem = MenuItem(strings.quit)

            startItem.isEnabled = false // server start running so this option start disabled
            startItem.addActionListener {
                startItem.isEnabled = false
                pauseItem.isEnabled = true
                startServer()
                trayIcon.toolTip = "Server $statusText"
            }
            pauseItem.addActionListener {
                startItem.isEnabled = true
                pauseItem.isEnabled = false
                pauseServer()
                trayIcon.toolTip = "Server $statusText"
            }

            visibilityItem.addActionListener(listener)

            popup.add(visibilityItem)
            popup.add(startItem)
            popup.add(pauseItem)
            popup.add(quitItem)

            try {
                tray.add(trayIcon);
            } catch (e: AWTException) {
                System.err.println(e);
            }
        }
    }

    @Synchronized private fun pairClient(packet: Packet){
        if (!clientList.containsKey(packet.sender)) {
            // TODO: multiple threads can touch this code at the same time, check if this is a problem
            clientList[packet.sender] = true
            clientListPanel.add(Client(packet.sender, packet.ip, true))
        }
    }

    private fun run() = runBlocking {
        println("Listening on port: $port")

        while (isRunning) {
            try {
                ds.receive(pkg) // wait a packet
            }
            catch (e: SocketException){
                break
            }

            launch(Dispatchers.Default) {
                try {
                    process(pkg)
                }
                catch (e: Exception){   // random packets may arrive, they will cause miscellaneous exceptions that we can just ignore for now
                    e.printStackTrace()
                }
            }.join()

            packetsCount++
            //showStats()
        }

        if (! ds.isClosed) {
            ds.close()
        }
    }

    fun startServer() {
        println("Starting server.")

        toolbar.playBtn.isEnabled = false
        toolbar.pauseBtn.isEnabled = true
        isRunning = true

        if (ds.isClosed)
            ds = DatagramSocket(port)

        GlobalScope.launch(Dispatchers.Main){
            // launch a new thread and leave the UI thread free
            launch(Dispatchers.Default) {
                run()
            }.join()
        }
    }

    private fun pauseServer() {
        println("Server paused.")
        toolbar.playBtn.isEnabled = true
        toolbar.pauseBtn.isEnabled = false
        isRunning = false
        ds.close()
    }

    private fun process(pkg: DatagramPacket) {
        val packet = Packet(pkg)

        pairClient(packet)

        if (clientList[packet.sender] == true) {
            when (packet.action) {
                MessageCodes.SLEEP -> {
                    Actions.hibernate()
                }
                MessageCodes.SHUTDOWN -> {
                    Actions.shutdown()
                }
                MessageCodes.ENTER -> {
                    Actions.keyEnter()
                }
                MessageCodes.BACKSPACE -> {
                    Actions.keyBack()
                }
                MessageCodes.UP -> {
                    Actions.keyUp()
                }
                MessageCodes.DOWN -> {
                    Actions.keyDown()
                }
                MessageCodes.LEFT -> {
                    Actions.keyLeft()
                }
                MessageCodes.RIGHT -> {
                    Actions.keyRight()
                }
                MessageCodes.VOL_UP -> {
                    Actions.mediaKeyVolUp()
                }
                MessageCodes.VOL_DOWN -> {
                    Actions.mediaKeyVolDown()
                }
                MessageCodes.PLAY -> {
                    Actions.mediaKeyPlay()
                }
                MessageCodes.STOP -> {
                    Actions.mediaKeyStop()
                }
                MessageCodes.PREVIOUS -> {
                    Actions.mediaKeyPrevious()
                }
                MessageCodes.NEXT -> {
                    Actions.mediaKeyNext()
                }
                MessageCodes.MUTE -> {
                    Actions.mediaKeyMute()
                }
                MessageCodes.TEXT_WRITE -> {
                    val text = packet.data
                    Actions.pasteText(text)
                }
                MessageCodes.ZOOM_IN -> {
                    Actions.zoomIn()
                }
                MessageCodes.ZOOM_OUT -> {
                    Actions.zoomOut()
                }
                MessageCodes.COPY -> {
                    Actions.copy()
                }
                MessageCodes.PASTE -> {
                    Actions.paste()
                }
                MessageCodes.LEFT_CLICK -> {
                    Actions.mouseLeftClick()
                }
                MessageCodes.RIGHT_CLICK -> {
                    Actions.mouseRightClick()
                }
                MessageCodes.LEFT_DOUBLE_CLICK -> {
                    Actions.mouseLDoubleClick()
                }
                MessageCodes.MOVE -> {
                    val data = packet.data
                    Actions.mouseMove(data)
                }
                MessageCodes.SCROLL_VER -> {
                    val data = packet.data
                    Actions.mouseScrollVer(data)
                }
                MessageCodes.SCROLL_HOR -> {
                    val data = packet.data
                    Actions.mouseScrollHor(data)
                }
                MessageCodes.LEFT_HOLD_START -> {
                    Actions.mouseLeftHold()
                }
                MessageCodes.LEFT_HOLD_END -> {
                    Actions.mouseLeftRelease()
                }
            }
        }
    }

    private fun changeState(state: Boolean) {
        val index = clientListPanel.getSelectedIndex()

        if (index > -1) {
            val client = clientListPanel.getSelectedName()
            clientList[client] = state
            clientListPanel.updateIndex(index, state)
            println("Client $client state updated")
        }
    }
}