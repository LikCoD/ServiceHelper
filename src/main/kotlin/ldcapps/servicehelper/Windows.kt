package ldcapps.servicehelper

import javafx.stage.Stage
import javafx.stage.StageStyle
import ldcapps.servicehelper.controllers.CreateAct
import ldcapps.servicehelper.controllers.MainView
import ldcapps.servicehelper.controllers.PrintBlankController
import ldcapps.servicehelper.controllers.PrintController
import ldcapps.servicehelper.controllers.detail.DetailView
import ldcapps.servicehelper.controllers.tools.ToolSelectorController
import tornadofx.find

object Windows {
    fun tools() = init<ToolSelectorController>(FXMLInfo.ToolSelector)
    fun print(stage: Stage) = init<PrintController>(FXMLInfo.Print, stage)
    fun act() = init<CreateAct>(FXMLInfo.CreateAct)
    fun blank() = init<PrintBlankController>(FXMLInfo.Blank)
    //fun login() = init<LoginController>(FXMLInfo.Login)

    lateinit var mainController: MainView

    var tools: ToolSelectorController? = null
        get() {
            if (field == null) field = init<ToolSelectorController>(FXMLInfo.ToolSelector)

            if (field == null) return null

            field!!.stage.show()
            field!!.stage.isAlwaysOnTop = true
            field!!.stage.isAlwaysOnTop = false

            return field
        }

    var details: ToolSelectorController? = null
        get() {
            find<DetailView>().openWindow()

            return field
        }

    private fun <T> init(FXMLInfo: FXMLInfo, primaryStage: Stage? = null): T? {
        try {
            val loader = fxmlLoader(FXMLInfo)
            loader.load<Stage>().apply {
                if (primaryStage != null) {
                    initStyle(StageStyle.UTILITY)
                    initOwner(primaryStage)
                    showAndWait()
                } else show()
            }

            return loader.getController()
        } catch (ex: Exception) {
            ex.printStackTrace()
            Dialogs.warning("Не удается открыть окно")
        }
        return null
    }
}