package ldcapps.servicehelper.controllers

import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.text.Font
import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.styles.MainStyle
import liklibs.db.sqList
import tornadofx.*
import kotlin.concurrent.thread

class LoadingView : View() {

    override val root: Parent = vbox(spacing = 10) {
        addClass(MainStyle.mainWindow)

        alignment = Pos.CENTER
        paddingAll = 10

        label("Загрузка..."){
            font = Font.font(24.0)
        }
        progressbar(-1.0){
            maxWidth = Double.MAX_VALUE
        }
    }

    init {
        thread {
            DataClasses.companies = sqList()
            DataClasses.individuals = sqList()
            DataClasses.owners = sqList()
            DataClasses.cars = sqList()

            DataClasses.reports = sqList()

            Platform.runLater { replaceWith<MainView>(sizeToScene = true, centerOnScreen = true) }
        }
    }
}