package ldcapps.servicehelper.controllers

import javafx.fxml.Initializable
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.stage.Stage
import ldcapps.servicehelper.*
import java.awt.Toolkit
import java.io.File
import java.net.URL
import java.util.*

class MainController : Initializable {
    lateinit var st: Stage
    lateinit var tabPane: TabPane

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        stage = st
        pane = tabPane

       // tabPane.tabDragPolicy = TabPane.TabDragPolicy.REORDER

        stage.setOnCloseRequest { if (!Dialogs.confirmation("Подтвердите выход")) it.consume() }

        if (Toolkit.getDefaultToolkit().screenSize.height.toDouble() < st.height - 50)
            stage.height = (Toolkit.getDefaultToolkit().screenSize.height - 50).toDouble()

        val mainTabLayout = loadFXML<HBox>(FXMLInfo.CarData)

        pane.tabs += Tab("Загрузка", mainTabLayout).apply {
            mainTab = this
            isClosable = false

            setOnSelectionChanged { if (isSelected) changeStageSize(FXMLInfo.CarData) }
            setTabText()
        }

        pane.selectionModel.select(pane.tabs.last())
    }

    fun fillOO(ooAndBill: OOController.OOAndBill, path: String = "") {
        pane.tabs += Tab("Загрузка").apply {
            setOnSelectionChanged { if (isSelected) changeStageSize(FXMLInfo.OOCollapsed) }
            setOnCloseRequest { if (!Dialogs.confirmation("Подтвердите выход")) it.consume() }
        }

        pane.selectionModel.select(pane.tabs.last())

        val loader = fxmlLoader(FXMLInfo.OO)
        val loadedPane = loader.load<AnchorPane>()
        loader.getController<OOController>().fill(ooAndBill, path)

        pane.tabs.last().content = loadedPane
    }

    companion object {
        var currentCashNumber = currentOONumber("Нал", "oo")
            set(value) {
                field = value
                setTabText()
            }
        var currentCashlessNumber = currentOONumber("Безнал", "oab")
            set(value) {
                field = value
                setTabText()
            }

        private var pane = TabPane()
        private var mainTab = Tab()

        lateinit var stage: Stage
            private set

        val selectedTab: Tab
            get() = pane.selectionModel.selectedItem

        private fun currentOONumber(folder: String, extension: String? = null, dir: String = settings.oosLocate): Int {
            val fileList = File(dir, folder).listFiles { _, name ->
                if (extension != null) name.endsWith(".$extension") else true
            }

            if (fileList == null || fileList.isEmpty()) return 1

            val numList = fileList.map { fromJSON<OOController.OOAndBill>(it).number }.sorted()
            for (i in 1 until numList.size)
                if (numList[i - 1] + 1 != numList[i]) return numList[i - 1] + 1

            return numList.last() + 1
        }

        private fun setTabText() {
            mainTab.text = "Б${currentCashlessNumber} Н${currentCashNumber}"
        }

        fun changeStageSize(fxmlInfo: FXMLInfo) {
            if (fxmlInfo.minWidth == null|| fxmlInfo.minHeight == null) return

            stage.width = fxmlInfo.minWidth
            stage.height = fxmlInfo.minHeight

            stage.minWidth = fxmlInfo.minWidth
            stage.minHeight = fxmlInfo.minHeight
        }

        fun closeSelectedTab() = pane.tabs.remove(selectedTab)
    }

}