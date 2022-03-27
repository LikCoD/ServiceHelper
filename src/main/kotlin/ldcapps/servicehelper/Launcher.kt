package ldcapps.servicehelper

import it.sauronsoftware.junique.AlreadyLockedException
import it.sauronsoftware.junique.JUnique
import javafx.application.Platform
import javafx.stage.Stage
import ldcapps.servicehelper.controllers.LoadingView
import ldcapps.servicehelper.controllers.MainView
import ldcapps.servicehelper.styles.MainStyle
import tornadofx.App
import tornadofx.launch
import kotlin.system.exitProcess

class MApp: App(LoadingView::class, MainStyle::class){
    override fun start(stage: Stage) {
        stage.minWidth = 400.0
        stage.width = 400.0

        stage.minHeight = 50.0
        super.start(stage)

        open(parameters.raw.firstOrNull(), stage)
    }
}

fun main(args: Array<String>) {
    try {
        JUnique.acquireLock("servicehelper") {
            Platform.runLater {
                val stage = (MainView.pane.scene.window as Stage)

                stage.isAlwaysOnTop = true
                stage.isAlwaysOnTop = false
            }

            open(it)

            it
        }
    } catch (e: AlreadyLockedException) {
        JUnique.sendMessage("servicehelper", args.firstOrNull())
        exitProcess(1)
    }

    launch<MApp>(args)
}