package ldcapps.servicehelper.controllers.tools

import javafx.fxml.Initializable
import javafx.scene.control.ToggleButton
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import ldcapps.servicehelper.Dialogs
import java.net.URL
import java.util.*

class ToolSelectorController : Initializable {
    lateinit var stage: Stage
    lateinit var pane: AnchorPane
    lateinit var addCarTb: ToggleButton
    lateinit var createContractTb: ToggleButton
    lateinit var redactDBTb: ToggleButton
    lateinit var getReportTb: ToggleButton
    lateinit var settingsTb: ToggleButton

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        //toolStage = stage
        toolPane = pane

        ToolsController.ADD_CAR.show<AddCarController>(addCarTb, stage)

        addCarTb.setOnAction { ToolsController.ADD_CAR.show<AddCarController>(addCarTb, stage) }
        createContractTb.setOnAction { (ToolsController.CREATE_CONTRACT.show<CreateContractController>(createContractTb, stage)).loadData(null) }
        redactDBTb.setOnAction { ToolsController.REDACT_DB.show(redactDBTb, stage) }
        getReportTb.setOnAction { ToolsController.GET_REPORT.show(getReportTb, stage) }
        settingsTb.setOnAction { ToolsController.SETTINGS.show(settingsTb, stage) }
        stage.setOnCloseRequest { if (!Dialogs.confirmation("Подтвердите выход")) it.consume() }
    }

    companion object {
        lateinit var toolPane: AnchorPane
        //lateinit var toolStage: Stage
    }
}
