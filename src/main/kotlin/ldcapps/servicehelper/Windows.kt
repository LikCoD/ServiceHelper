package ldcapps.servicehelper

import it.sauronsoftware.junique.AlreadyLockedException
import it.sauronsoftware.junique.JUnique
import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import javafx.stage.StageStyle
import kotlinx.serialization.ExperimentalSerializationApi
import ldcapps.servicehelper.controllers.*
import ldcapps.servicehelper.controllers.tools.ToolSelectorController
import kotlin.system.exitProcess

var args: Array<String> = arrayOf()

@ExperimentalSerializationApi
fun main(arguments: Array<String>) {
    try {
        JUnique.acquireLock("servicehelperid") {
            Platform.runLater {
                Windows.ooController?.stage?.isAlwaysOnTop = true
                Windows.ooController?.stage?.isAlwaysOnTop = false
            }

            if (it != null) open(it)

            it
        }
    } catch (e: AlreadyLockedException) {
        JUnique.sendMessage("servicehelperid", args.firstOrNull())
        exitProcess(1)
    }

    args = arguments
    Application.launch(Windows::class.java, *args)
}

@ExperimentalSerializationApi
class Windows : Application() {

    override fun start(primaryStage: Stage) {
        val loader = fxmlLoader(FXMLInfo.Main)
        val stage = loader.load<Stage>()
        ooController = loader.getController()
        stage.show()
    }

    companion object {
        var ooController: MainController? = null/* by lazy {
            Signup.type = Signup.Companion.Type.SETTINGS
            init<MainController>(FXML.Main)
        }*/

        fun tools() = init<ToolSelectorController>(FXMLInfo.ToolSelector)
        fun print(stage: Stage) = init<PrintController>(FXMLInfo.Print, stage)
        fun act() = init<CreateAct>(FXMLInfo.CreateAct)
        fun blank() = init<PrintBlankController>(FXMLInfo.Blank)
        //fun login() = init<LoginController>(FXMLInfo.Login)

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
}