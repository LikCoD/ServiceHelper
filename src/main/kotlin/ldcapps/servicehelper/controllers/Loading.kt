package ldcapps.servicehelper.controllers

import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.print.PageOrientation
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import javafx.stage.StageStyle
import ldcapps.servicehelper.*
import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.db.SQList
import java.awt.Toolkit
import java.io.File
import java.net.URL
import java.util.*
import kotlin.concurrent.thread

class Loading : Initializable {
    lateinit var stage: Stage
    lateinit var statusLb: Label
    lateinit var tryAgainBtn: Button
    lateinit var changeBtn: Button
    lateinit var continueOfflineBtn: Button
    lateinit var progress: ProgressBar

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        stage.setOnShowing { load() }

        continueOfflineBtn.setOnAction {
            if (File(".user").exists()) open(args.getOrNull(0))
            else Platform.runLater { statusLb.text = "Пользователь не сохранен" }

            stage.close()
        }

        tryAgainBtn.setOnAction {
            load()

            statusLb.text = "Загрузка..."

            progress.isVisible = true
            progress.prefHeight = -1.0

            tryAgainBtn.isVisible = false
        }

        changeBtn.setOnAction {
            FXMLLoader(Loading::class.java.classLoader.getResource("fxml/ChangeDBHost.fxml")).load<Stage>()
                .apply { initStyle(StageStyle.UTILITY) }.show()
        }
    }

    private fun load() {
        thread {
            try {
                URL("https://oremotemysql.com/login.phpp").openConnection().apply { connectTimeout = 2000 }.connect()

                Platform.runLater {
                    statusLb.text = "Проверка..."
                    continueOfflineBtn.isDisable = true
                }

                if (DataClasses.db?.checkToken() == true) {
                    isOnline = true
                    Platform.runLater { statusLb.text = "Синхронизация..." }

                    fun <E : Any> addDiffEls(list1: MutableList<E>, list2: SQList<E>) =
                        list1.forEach { if (!list2.contains(it)) list2.add(it) }

                    addDiffEls(arrFromJSON(".report"), DataClasses.reports)
                    addDiffEls(arrFromJSON(".cars"), DataClasses.cars)
                    addDiffEls(arrFromJSON(".companies"), DataClasses.companies)
                    addDiffEls(arrFromJSON(".owners"), DataClasses.owners)
                    addDiffEls(arrFromJSON(".individuals"), DataClasses.individuals)

                    if (File(".user").exists()) {
                        toJSON(".report", DataClasses.reports.list)
                        toJSON(".cars", DataClasses.cars.list)
                        toJSON(".companies", DataClasses.companies.list)
                        toJSON(".owners", DataClasses.owners.list)
                        toJSON(".individuals", DataClasses.individuals.list)
                    }
                }
                Platform.runLater {
                    open(args.getOrNull(0))
                    stage.close()
                }
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

