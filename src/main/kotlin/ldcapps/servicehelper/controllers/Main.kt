package ldcapps.servicehelper.controllers

import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.stage.Stage
import ldcapps.servicehelper.Dialogs
import ldcapps.servicehelper.Windows
import ldcapps.servicehelper.fromJSON
import ldcapps.servicehelper.settings
import java.awt.Toolkit
import java.io.File
import java.net.URL
import java.util.*

class Main : Initializable {
    lateinit var st: Stage
    lateinit var tabPane: TabPane

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        stage = st
        if (Toolkit.getDefaultToolkit().screenSize.height.toDouble() < st.height - 50)
            stage.height = (Toolkit.getDefaultToolkit().screenSize.height - 50).toDouble()
        stage.setOnCloseRequest { if (!Dialogs.confirmation("Подтвердите выход")) it.consume() }

        pane = tabPane
        pane.tabClosingPolicy = TabPane.TabClosingPolicy.SELECTED_TAB
        pane.tabDragPolicy = TabPane.TabDragPolicy.REORDER

        pane.tabs += Tab(
            "Б${lastCashlessNumber} Н${lastCashNumber}",
            FXMLLoader(Windows::class.java.classLoader.getResource("fxml/CarData.fxml")).load<HBox>()
        ).apply {
            isClosable = false

            setOnSelectionChanged {
                if (isSelected) {
                    stage.height = 335.0
                    stage.width = 275.0

                    stage.minHeight = 335.0
                    stage.minWidth = 275.0
                }
            }

            mainTab = this
        }

        pane.selectionModel.select(pane.tabs.last())
    }

    fun fill(oo: OO.OOAndBill, path: String = "") {
        pane.tabs += Tab("Загрузка").apply {
            setOnSelectionChanged {
                if (isSelected) {
                    stage.height = 700.0
                    stage.width = 1100.0

                    stage.minHeight = 700.0
                    stage.minWidth = 1100.0
                }
            }
            setOnCloseRequest { if (!Dialogs.confirmation("Подтвердите выход")) it.consume() }
        }

        pane.selectionModel.select(pane.tabs.last())

        val loader = FXMLLoader(Windows::class.java.classLoader.getResource("fxml/OO.fxml"))
        val aPane = loader.load<AnchorPane>()
        loader.getController<OO>().fill(oo, path)

        pane.tabs.last().content = aPane
    }

    companion object {
        private fun getLastOONumber(dir: String): Int {
            try {
                val list =
                    File("${settings.oosLocate}\\$dir").list { _, name -> name.endsWith(".oab") || name.endsWith(".oo") }
                        ?.map { fromJSON<OO.OOAndBill>("${settings.oosLocate}\\$dir\\$it").number }?.sorted()
                list?.forEachIndexed { i, el ->
                    if (i > 0 && list[i - 1] + 1 != el) return list[i - 1] + 1
                }
                return list?.maxOrNull()?.plus(1) ?: 1
            }catch (ex: Exception){
                ex.printStackTrace()
                return 1
            }
        }

        var lastCashNumber = getLastOONumber("Нал")
            set(value) {
                mainTab.text = "Б$lastCashlessNumber Н$value"
                field = value
            }
        var lastCashlessNumber = getLastOONumber("Безнал")
            set(value) {
                mainTab.text = "Б$value Н$lastCashNumber"
                field = value
            }

        private var pane = TabPane()
        private var mainTab = Tab()

        lateinit var stage: Stage
            private set

        val selectedTab: Tab
            get() = pane.selectionModel.selectedItem

        fun closeSelectedTab() = pane.tabs.remove(selectedTab)

    }

}