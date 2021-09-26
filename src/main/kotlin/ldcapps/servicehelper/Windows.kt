package ldcapps.servicehelper

import it.sauronsoftware.junique.AlreadyLockedException
import it.sauronsoftware.junique.JUnique
import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import javafx.stage.StageStyle
import ldcapps.servicehelper.controllers.*
import ldcapps.servicehelper.controllers.tools.Signup
import ldcapps.servicehelper.controllers.tools.ToolSelector
import kotlin.system.exitProcess

var isOnline = false

var args: Array<String> = arrayOf()

fun main(arguments: Array<String>) {
    try {
        JUnique.acquireLock("servicehelperid") {
            Platform.runLater {
                Windows.ooController?.st?.isAlwaysOnTop = true
                Windows.ooController?.st?.isAlwaysOnTop = false
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

class Windows : Application() {

    override fun start(stage: Stage) = loadFXML<Stage>(FXML.Main).show()

    companion object {
        val ooController: MainController? by lazy {
            Signup.type = Signup.Companion.Type.SETTINGS
            init<MainController>(FXML.Main)
        }

        fun tools() = init<ToolSelector>(FXML.ToolSelector)
        fun print(stage: Stage) = init<Print>(FXML.Print, stage)
        fun act() = init<CreateAct>(FXML.CreateAct)
        fun blank() = init<Blank>(FXML.Blank)
        fun login() = init<Login>(FXML.Login)

        private fun <T> init(FXML: FXML, primaryStage: Stage? = null): T? {
            try {
                val loader = fxmlLoader(FXML)
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