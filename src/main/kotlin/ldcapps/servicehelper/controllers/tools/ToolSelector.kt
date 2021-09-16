package ldcapps.servicehelper.controllers.tools

import javafx.fxml.Initializable
import javafx.scene.control.ToggleButton
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import ldcapps.servicehelper.Dialogs
import java.net.URL
import java.util.*

class ToolSelector : Initializable {
    lateinit var stage: Stage
    lateinit var pane: AnchorPane
    lateinit var addCarTb: ToggleButton
    lateinit var createContractTb: ToggleButton
    lateinit var redactDBTb: ToggleButton
    lateinit var getReportTb: ToggleButton
    lateinit var settingsTb: ToggleButton

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        toolStage = stage
        toolPane = pane

        Panes.ADD_CAR.show<AddCar>(addCarTb)

        addCarTb.setOnAction { Panes.ADD_CAR.show<AddCar>(addCarTb) }
        createContractTb.setOnAction { (Panes.CREATE_CONTRACT.show<CreateContract>(createContractTb)).loadData(null) }
        redactDBTb.setOnAction { Panes.REDACT_DB.show(redactDBTb) }
        getReportTb.setOnAction { Panes.GET_REPORT.show(getReportTb) }
        settingsTb.setOnAction { Panes.SETTINGS.show(settingsTb) }
        stage.setOnCloseRequest { if (!Dialogs.confirmation("Подтвердите выход")) it.consume() }
    }

    companion object {
        lateinit var toolPane: AnchorPane
        lateinit var toolStage: Stage
    }
}
