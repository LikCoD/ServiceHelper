package ldcapps.servicehelper.controllers

import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventTarget
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Priority
import javafx.stage.Stage
import ldcapps.servicehelper.*
import ldcapps.servicehelper.controllers.tools.ToolsController
import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.styles.MainStyle
import ldclibs.javafx.controls.autocompletedTextfield
import ldclibs.javafx.controls.intTextfield
import liklibs.db.toSQL
import tornadofx.*
import java.awt.Toolkit
import java.io.File
import java.time.LocalDate

class MainView : View() {
    private var registrationDateProperty = SimpleObjectProperty(LocalDate.now())
    private var registrationDate by registrationDateProperty
    private var executionDateProperty = SimpleObjectProperty(LocalDate.now())
    private var executionDate by executionDateProperty

    private var carProperty = SimpleObjectProperty<DataClasses.Car>()
    private var car by carProperty
    private var mileageProperty = SimpleObjectProperty<String>()
    private var mileage by mileageProperty
    private var numberProperty = SimpleObjectProperty<String>()
    private var number by numberProperty

    private lateinit var keyField: TextField
    private lateinit var mileageField: TextField
    private lateinit var numberField: TextField

    override val root = tabpane {
        Windows.mainController = this@MainView
        pane = this

        addClass(MainStyle.mainWindow)

        primaryStage.setOnCloseRequest { if (!Dialogs.confirmation("Подтвердите выход")) it.consume() }

        if (Toolkit.getDefaultToolkit().screenSize.height.toDouble() < primaryStage.height - 50)
            primaryStage.height = (Toolkit.getDefaultToolkit().screenSize.height - 50).toDouble()

        tab("Main") {
            mainTab = this
            isClosable = false

            setOnSelectionChanged { if (isSelected) changeStageSize(FXMLInfo.CarData, pane) }
            updateMainTabText()

            hbox {
                vbox {
                    toolBtn("open") {
                        open(Dialogs.getFile(primaryStage, settings.oosLocate), primaryStage)
                    }
                    toolBtn("cloud-storage") {
                        Windows.export
                    }

                    toolBtn("tools") {
                        Windows.tools
                    }
                    toolBtn("details") {
                        Windows.details
                    }
                    toolBtn("act") {
                        Windows.act()
                    }
                    toolBtn("blank") {
                        Windows.blank()
                    }
                }
                vbox(10) {
                    hgrow = Priority.ALWAYS
                    maxWidth = Double.MAX_VALUE

                    paddingAll = 20

                    hbox(10, Pos.CENTER_LEFT) {
                        imageview("drawable/avatar.png") {
                            fitHeight = 35.0
                            fitWidth = 35.0
                            onLeftClick {
                                val toolsController = Windows.tools ?: return@onLeftClick
                                ToolsController.SETTINGS.show(toolsController.settingsTb, toolsController.stage)
                            }
                        }
                        label(DataClasses.user.name)

                        if (!DataClasses.cars.utils.onlineDB.isAvailable) {
                            anchorpane {
                                hgrow = Priority.ALWAYS
                                maxWidth = Double.MAX_VALUE

                                prefHeight = 25.0

                                imageview("drawable/no-signal.png") {
                                    anchorpaneConstraints {
                                        topAnchor = 5
                                        bottomAnchor = 5
                                        rightAnchor = 0
                                    }

                                    fitHeight = 25.0
                                    fitWidth = 25.0
                                    tooltip("Оффлайн")
                                }
                            }
                        }
                    }

                    datepicker(registrationDateProperty) {
                        hgrow = Priority.ALWAYS
                        maxWidth = Double.MAX_VALUE
                        isEditable = false
                        setOnAction { executionDate = registrationDate }
                    }
                    datepicker(executionDateProperty) {
                        hgrow = Priority.ALWAYS
                        maxWidth = Double.MAX_VALUE
                        isEditable = false
                    }
                    keyField = autocompletedTextfield(carProperty, items = DataClasses.cars, getString = { it.key }) {
                        hgrow = Priority.ALWAYS
                        maxWidth = Double.MAX_VALUE
                        promptText = "Ключ"
                    }
                    hbox(10) {
                        mileageField = intTextfield(mileageProperty) {
                            hgrow = Priority.ALWAYS
                            maxWidth = Double.MAX_VALUE
                            prefWidth = 100.0

                            promptText = "Пробег"
                        }
                        numberField = intTextfield(numberProperty) {
                            prefWidth = 50.0
                            promptText = "#"
                        }
                    }
                    button("Подтвердить") {
                        addClass(MainStyle.confirmButton)

                        hgrow = Priority.ALWAYS
                        maxWidth = Double.MAX_VALUE

                        action {
                            initOO()
                        }
                    }
                }
            }
        }
    }

    private fun initOO() {
        if (car == null) {
            Animations.emptyNode(keyField)
            return
        }

        try {
            val ooAndBill = OOController.OOAndBill(
                registrationDate = registrationDate.toSQL(),
                executionDate = executionDate.toSQL(),
                car = car,
                carMileage = mileage?.toIntOrNull(),
                executor = DataClasses.user.getExecutor()
            )

            ooAndBill.number = if (car.individualId == null) currentCashlessNumber++
            else currentCashNumber++

            if (number != null && number != "") ooAndBill.number = number?.toIntOrNull() ?: 0

            fillOO(ooAndBill)

            registrationDate = LocalDate.now()
            executionDate = LocalDate.now()
            car = null
            keyField.text = ""
            mileageField.text = ""
            numberField.text = ""
            mileage = ""
            number = ""
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun EventTarget.toolBtn(name: String, action: () -> Unit) {
        button {
            addClass(MainStyle.toolButton)
            vgrow = Priority.ALWAYS
            setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE)
            imageview("drawable/$name.png") {
                fitHeight = 40.0
                fitWidth = 40.0
            }

            action { action() }
        }
    }

    fun fillOO(ooAndBill: OOController.OOAndBill, path: String = "", id: Int? = null) {
        val loader = fxmlLoader(FXMLInfo.OO)
        val loadedPane = loader.load<AnchorPane>()
        val controller = loader.getController<OOController>()

        root.tabs += Tab("Загрузка").apply {
            setOnSelectionChanged {
                if (!isSelected) return@setOnSelectionChanged


                changeStageSize(
                    if (controller.ooAndBillScroll.isVisible) FXMLInfo.OO else FXMLInfo.OOCollapsed, tabPane
                )
            }
            setOnCloseRequest { if (!Dialogs.confirmation("Подтвердите выход")) it.consume() }
        }

        root.selectionModel.select(root.tabs.last())
        root.tabs.last().content = loadedPane

        controller.fill(ooAndBill, path, id)
    }

    companion object {
        var currentCashNumber = currentOONumber("Нал", "oo")
            set(value) {
                field = value
                updateMainTabText()
            }
        var currentCashlessNumber = currentOONumber("Безнал", "oab")
            set(value) {
                field = value
                updateMainTabText()
            }

        var pane = TabPane()
        private var mainTab = Tab()

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

        private fun updateMainTabText() {
            mainTab.text = "Б${currentCashlessNumber} Н${currentCashNumber}"
        }

        fun changeStageSize(fxmlInfo: FXMLInfo, node: Node) {
            if (fxmlInfo.minWidth == null || fxmlInfo.minHeight == null) return

            val stage = node.scene.window as Stage

            stage.width = fxmlInfo.minWidth
            stage.height = fxmlInfo.minHeight

            stage.minWidth = fxmlInfo.minWidth
            stage.minHeight = fxmlInfo.minHeight
        }

        fun closeSelectedTab() = pane.tabs.remove(selectedTab)
    }
}