package ldcapps.servicehelper.controllers.tools

import javafx.fxml.FXMLLoader
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ToggleButton
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import ldcapps.servicehelper.Dialogs
import ldcapps.servicehelper.controllers.tools.ToolSelector.Companion.toolPane
import ldcapps.servicehelper.controllers.tools.ToolSelector.Companion.toolStage
import ldcapps.servicehelper.db.DataClasses

enum class Panes(
    private val fileName: String,
    private val text: String,
    private val isMaximized: Boolean = false,
    private val minWidth: Double = 650.0,
    private val minHeight: Double = 350.0
) {

    ADD_CAR("Add car", "Добавить авто"),
    CREATE_CONTRACT("Create contract", "Создать договор", true, 1000.0),
    REDACT_DB("Redact DB", "Редактировать БД", true, 800.0),
    GET_REPORT("Get report", "Получить  отчет", true, 800.0),
    SETTINGS("Signup", "Настройки", false, 750.0, 730.0);

    fun <T> show(btn: ToggleButton): T {
        val controller = update<T>()

        toolStage.title = text
        toolStage.isMaximized = isMaximized
        toolStage.minWidth = minWidth
        toolStage.minHeight = minHeight

        btn.isSelected = true

        if (!toolStage.isMaximized) {
            toolStage.width = minWidth
            toolStage.height = minHeight
        }

        return controller
    }

    fun <T> update(): T {
        val loader = FXMLLoader(javaClass.classLoader.getResource("fxml/Tools/$fileName.fxml"))
        val pane = loader.load<AnchorPane>()

        if (fileName == "Signup")
            (pane.children[0] as VBox).children.add(0, HBox().apply {
                alignment = Pos.CENTER_RIGHT
                spacing = 10.0

                children.addAll(
                    Button("Удалить").apply {
                        prefWidth = 70.0
                        id = "delete"
                        stylesheets += javaClass.classLoader.getResource("css/toolsBtns.css")?.toURI().toString()
                        setOnAction {
                            if (Dialogs.password { DataClasses.db?.checkPassword(it) == true }) {
                                DataClasses.db?.deleteUser()
                                DataClasses.delete()
                            } else
                                Dialogs.information("Повторите попытку")
                        }
                    },

                    Button("Выйти").apply {
                        prefWidth = 70.0
                        id = "login"
                        stylesheets += javaClass.classLoader.getResource("css/toolsBtns.css")?.toURI().toString()
                        setOnAction {
                            if (Dialogs.password { DataClasses.db?.checkPassword(it) == true })
                                DataClasses.delete()
                            else
                                Dialogs.information("Повторите попытку")
                        }
                    })
            })

        toolPane.children.clear()
        toolPane.children.add(pane)

        return loader.getController()
    }
}