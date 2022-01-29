package ldcapps.servicehelper.controllers.tools

import javafx.fxml.FXMLLoader
import javafx.scene.control.ToggleButton
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import javafx.stage.Window
import ldcapps.servicehelper.FXMLInfo
import ldcapps.servicehelper.controllers.tools.ToolSelectorController.Companion.toolPane

enum class ToolsController(
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

        toolPane.children.clear()
        toolPane.children.add(pane)

        return loader.getController()
    }
}