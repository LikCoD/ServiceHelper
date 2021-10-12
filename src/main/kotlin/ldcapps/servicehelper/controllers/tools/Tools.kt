package ldcapps.servicehelper.controllers.tools

import javafx.fxml.FXMLLoader
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ToggleButton
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.stage.Window
import ldcapps.servicehelper.Dialogs
import ldcapps.servicehelper.FXMLInfo
import ldcapps.servicehelper.controllers.tools.ToolSelectorController.Companion.toolPane
import ldcapps.servicehelper.db.DataClasses

enum class Tools(
    private val info: FXMLInfo,
    private val text: String,
    private val isMaximized: Boolean = false,
) {

    ADD_CAR(FXMLInfo.TOOL_ADD_CAR, "Добавить авто"),
    CREATE_CONTRACT(FXMLInfo.TOOL_CREATE_CONTRACT, "Создать договор", true),
    REDACT_DB(FXMLInfo.TOOL_REDACT_DB, "Редактировать БД", true),
    GET_REPORT(FXMLInfo.TOOL_GET_REPORT, "Получить  отчет", true),
    SETTINGS(FXMLInfo.TOOL_SING_UP, "Настройки", false);

    fun <T> show(toggleButton: ToggleButton, window: Window): T {
        val controller = update<T>()

        if (info.minWidth == null || info.minHeight == null) return controller

        val stage = window as Stage

        stage.title = text
        stage.isMaximized = isMaximized
        stage.minWidth = info.minWidth
        stage.minHeight = info.minHeight

        toggleButton.isSelected = true

        if (!stage.isMaximized) {
            stage.width = info.minWidth
            stage.height = info.minHeight
        }

        return controller
    }

    fun <T> update(): T {
        val loader = FXMLLoader(javaClass.classLoader.getResource("fxml/${info.path}.fxml"))
        val pane = loader.load<AnchorPane>()

        if (info == FXMLInfo.TOOL_SING_UP)
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