package xyz.uchiha.remotto.gui

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.TitledBorder
import xyz.uchiha.remotto.gui.ReadableStrings.Companion.strings

class ClientListPanel(private val panel: JPanel = JPanel()) : JScrollPane(panel) {
    private var listModel: DefaultListModel<Client> = DefaultListModel<Client>()
    var list: JList<Client> = JList(listModel)

    init {
        panel.layout = BorderLayout()//(panel, BoxLayout.Y_AXIS)
        horizontalScrollBarPolicy = 31
        minimumSize = Dimension(200, 400)
        preferredSize = Dimension(200, 400)
        //panel.add(Box.createVerticalGlue())
        border = BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(),
            strings.clients,
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            null,
            null
        )

        val color = UIManager.getColor("Panel.background")  // remove teh white square bellow list
        list.background = Color(color.rgb)
        list.selectionMode = ListSelectionModel.SINGLE_SELECTION
        list.cellRenderer = ClientListItemRenderer()
        list.isOpaque = false
        //panel.add(list)
        val a = JPanel()
        a.add(JLabel("No clients have been paired yet."))
        panel.add(a)
    }

    fun add(item: Client){
        if(listModel.size() == 0){
            panel.remove(0)
            panel.add(list)
        }

        listModel.addElement(item)
    }

    fun getSelectedIndex() : Int{
        return list.selectedIndex
    }

    fun getSelectedName() : String{
        return listModel[list.selectedIndex].name
    }

    fun updateIndex(index: Int, isEnabled: Boolean){
        listModel[index].isEnabled = isEnabled
        list.updateUI()
    }
}