package ldcapps.servicehelper.controllers

import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.stage.Stage
import javafx.stage.StageStyle
import kotlinx.serialization.ExperimentalSerializationApi
import java.net.URL
import java.util.*
import kotlin.concurrent.thread

@ExperimentalSerializationApi
class LoadingController : Initializable {
    lateinit var stage: Stage
    lateinit var statusLb: Label
    lateinit var tryAgainBtn: Button
    lateinit var changeBtn: Button
    lateinit var continueOfflineBtn: Button
    lateinit var progress: ProgressBar

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        stage.setOnShowing { load() }

        continueOfflineBtn.setOnAction {
/*            if (File(".user").exists()) open(args.getOrNull(0))
            else Platform.runLater { statusLb.text = "Пользователь не сохранен" }

            stage.close()*/
        }

        tryAgainBtn.setOnAction {
            load()

            statusLb.text = "Загрузка..."

            progress.isVisible = true
            progress.prefHeight = -1.0

            tryAgainBtn.isVisible = false
        }

        changeBtn.setOnAction {
            FXMLLoader(LoadingController::class.java.classLoader.getResource("fxml/ChangeDBHost.fxml")).load<Stage>()
                .apply { initStyle(StageStyle.UTILITY) }.show()
        }
    }

    private fun load() {
        thread {
            try {

            } catch (e: Exception) {
                Platform.runLater {
                    statusLb.text = "Ошибка подключения"

                    progress.isVisible = false
                    progress.prefHeight = 0.0

                    tryAgainBtn.isVisible = true
                }
            }
        }
    }
}

