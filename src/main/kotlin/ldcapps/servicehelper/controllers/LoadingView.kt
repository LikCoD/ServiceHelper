package ldcapps.servicehelper.controllers

import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.text.Font
import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.db.Orders
import ldcapps.servicehelper.styles.MainStyle
import liklibs.db.sqList
import tornadofx.*
import kotlin.concurrent.thread


class LoadingView : View() {

    private lateinit var stateLabel: Label

    override val root: Parent = vbox(spacing = 10) {
        addClass(MainStyle.mainWindow)

        alignment = Pos.CENTER
        paddingAll = 10

        anchorpane {
            label("Загрузка...") {
                anchorpaneConstraints {
                    rightAnchor = 5
                    leftAnchor = 5
                }
                font = Font.font(24.0)
            }
            stateLabel = label("Подключение") {
                anchorpaneConstraints {
                    rightAnchor = 5
                    bottomAnchor = 0
                }
            }
        }
        progressbar(-1.0) {
            maxWidth = Double.MAX_VALUE
        }
    }

    init {
        thread {
            setState("Компании")
            DataClasses.companies = sqList(conflictResolver = ConflictResolverView::addConflict)

            setState("Физ. лица")
            DataClasses.individuals = sqList(conflictResolver = ConflictResolverView::addConflict)

            setState("Владельцы")
            DataClasses.owners = sqList(conflictResolver = ConflictResolverView::addConflict)

            setState("Авто")
            DataClasses.cars = sqList(conflictResolver = ConflictResolverView::addConflict)

            setState("Отчеты")
            DataClasses.reports = sqList(conflictResolver = ConflictResolverView::addConflict)

            setState("Заказ-наряды")
            Orders.ordersAndBills = sqList(conflictResolver = ConflictResolverView::addConflict)

            setState("Конфликт")
            ConflictResolverView.onAllResolved = {
                Platform.runLater { replaceWith<MainView>(sizeToScene = true, centerOnScreen = true) }
            }

            Platform.runLater { ConflictResolverView.open() }
        }
    }

    private fun setState(state: String) {
        Platform.runLater { stateLabel.text = state }
    }
}