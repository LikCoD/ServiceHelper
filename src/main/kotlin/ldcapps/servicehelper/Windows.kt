package ldcapps.servicehelper

import it.sauronsoftware.junique.AlreadyLockedException
import it.sauronsoftware.junique.JUnique
import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
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

    override fun start(stage: Stage) =
        FXMLLoader(Windows::class.java.classLoader.getResource("fxml/Main.fxml")).load<Stage>().show()

    companion object {
        val ooController: Main? by lazy {
            Signup.type = Signup.Companion.Type.SETTINGS
            init<Main>("Main")
        }

        fun tools() = init<ToolSelector>("Tools/Main")
        fun print(stage: Stage) = init<Print>("Print", stage)
        fun act() = init<CreateAct>("Create act")
        fun blank() = init<Blank>("Blank")
        fun login() = init<Login>("Login")

        private fun <T> init(file: String, primaryStage: Stage? = null): T? {
            try {
                val loader = FXMLLoader(Windows::class.java.classLoader.getResource("fxml/$file.fxml"))
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
