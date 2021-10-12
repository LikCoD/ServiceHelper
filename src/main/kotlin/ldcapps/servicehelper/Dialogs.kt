package ldcapps.servicehelper

import javafx.geometry.Pos
import javafx.print.Collation
import javafx.print.PageOrientation
import javafx.print.PrinterJob
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.scene.transform.Scale
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.Stage
import ldcapps.servicehelper.controllers.Print
import ldcapps.servicehelper.controllers.Print.Companion.pageOrientation
import ldcapps.servicehelper.db.DataClasses
import java.io.File

class Dialogs {
    companion object {
        fun warning(text: String) = getAlert(Alert.AlertType.WARNING, "Внимание", text).showAndWait()!!

        fun information(text: String) = getAlert(Alert.AlertType.INFORMATION, "Информация", text).showAndWait()!!

        inline fun confirmation(text: String, code: () -> Unit = {}): Boolean {
            val alert = getAlert(Alert.AlertType.CONFIRMATION, "Подтверждение", text)
            alert.buttonTypes.setAll(
                ButtonType("Подтвердить", ButtonBar.ButtonData.YES), ButtonType("Отмена", ButtonBar.ButtonData.NO)
            )
            val buttonData = alert.showAndWait().get().buttonData
            return if (buttonData == ButtonBar.ButtonData.YES) {
                code()
                true
            } else false
        }

        fun password(code: (String) -> Boolean): Boolean {
            val dialog = Dialog<String>()
            val passwordField = PasswordField()

            dialog.title = "Подтвердите пароль"
            dialog.headerText = "Для продолжения введите пароль"

            dialog.dialogPane.buttonTypes.addAll(ButtonType.OK, ButtonType.CANCEL)

            dialog.dialogPane.content = HBox().apply {
                spacing = 10.0
                alignment = Pos.CENTER_LEFT
                children.addAll(Label("Пароль"), passwordField)
            }

            dialog.showAndWait()

            if (passwordField.text != "")
                return code(passwordField.text)

            return false
        }

        data class Filter(val topMargin: Int, val rightMargin: Int, val result: List<Int>)

        fun filter(type: Int, vararg els: String): Filter {
            val dialog = Dialog<String>()
            dialog.title = "Фильтр"
            dialog.dialogPane.buttonTypes.addAll(ButtonType.OK)

            val excelTab = DataClasses.excelTabs[type]

            val rightMargin = Spinner<Int>().apply {
                prefWidth = 60.0
                valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, excelTab.rightMargin)
            }
            val topMargin = Spinner<Int>().apply {
                prefWidth = 60.0
                valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, excelTab.topMargin)
            }

            val tabPane = TabPane().apply {
                tabDragPolicy = TabPane.TabDragPolicy.REORDER
                tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

                els.forEach { _ -> tabs.add(Tab()) }

                els.forEachIndexed { index, it ->
                    tabs[excelTab.tabsSequence?.indexOf(it) ?: index].text = it
                }
            }
            val saveCb = CheckBox("Сохранить")

            dialog.dialogPane.content = VBox(10.0).apply {
                children.addAll(saveCb, HBox(10.0).apply {
                    alignment = Pos.CENTER

                    children.addAll(
                        Text("Отступы:    "),
                        HBox(5.0).apply {
                            alignment = Pos.CENTER
                            children.addAll(Text("⬇"), topMargin)
                        },
                        HBox(5.0).apply {
                            alignment = Pos.CENTER
                            children.addAll(Text("➡"), rightMargin)
                        }
                    )
                }, tabPane)
            }

            dialog.showAndWait()

            val res = mutableListOf<Int>()
            val tabsText = tabPane.tabs.map { it.text }

            els.forEach { res.add(tabsText.indexOf(it)) }

            if (saveCb.isSelected) {
                DataClasses.excelTabs[type] = DataClasses.ExcelTabs(
                    topMargin.value,
                    rightMargin.value,
                    tabPane.tabs.map { it.text }
                )

                toJSON(".excelTabs", DataClasses.excelTabs)
            }

            return Filter(topMargin.value, rightMargin.value, res)
        }

        fun getAlert(type: Alert.AlertType, title: String, text: String): Alert {
            val alert = Alert(type)
            alert.title = title
            alert.headerText = text
            return alert
        }

        fun print(stage: Stage, orientation: PageOrientation, vararg nodes: Node): Boolean {
            pageOrientation = orientation
            with(Windows.print(stage) ?: Print()) {
                if (confirmPrint) {
                    data class NodeData(val isVisible: Boolean, val lX: Double, val lY: Double, val scale: Scale)

                    val nodesData = mutableListOf<NodeData>()

                    nodes.forEach {
                        nodesData.add(
                            NodeData(
                                it.isVisible,
                                it.layoutX,
                                it.layoutY,
                                Scale(it.scaleX, it.scaleY, it.scaleX)
                            )
                        )

                        it.isVisible = false
                        it.layoutX = 0.0
                        it.layoutY = 0.0
                    }

                    var i = 0
                    val pageLayout = printer.createPageLayout(paper, orientation, fields)
                    val job = PrinterJob.createPrinterJob(printer)

                    job.jobSettings.collation =
                        if (solveToCopiesCb.isSelected) Collation.COLLATED else Collation.UNCOLLATED
                    job.jobSettings.copies = copies
                    job.jobSettings.printSides = printType

                    do {
                        nodes[i].isVisible = true

                        val scaleW = pageLayout.printableWidth / nodes[i].boundsInParent.width
                        val scaleH = pageLayout.printableHeight / nodes[i].boundsInParent.height
                        val scale = if (scaleW < scaleH) scaleW else scaleH

                        nodes[i].transforms.add(Scale(scale, scale))
                        job.printPage(pageLayout, nodes[i])

                        nodes[i].isVisible = nodesData[i].isVisible
                        nodes[i].layoutX = nodesData[i].lX
                        nodes[i].layoutY = nodesData[i].lY
                        nodes[i].transforms.add(nodesData[i].scale)

                        i++
                    } while (i in nodes.indices && printArea != 0)

                    job.endJob()
                }

                return confirmPrint
            }
        }

        fun getFile(
            stage: Stage,
            path: String? = null,
            vararg extensions: Pair<String, String> = arrayOf(
                "oab" to "Безнал Заказ-Наряд",
                "oo" to "Нал Заказ-Наряд",
                "act" to "Акт",
                "contract" to "Договор",
                "db" to "БД",
                "report" to "Отчет",
                "settings" to "Настройки",
                "data" to "Данные"
            )
        ): String? {
            val fc = FileChooser()

            FileChooser.ExtensionFilter("*", extensions.map { "*.${it.first}" })

            extensions.forEach {
                fc.extensionFilters.add(FileChooser.ExtensionFilter(it.second, "*.${it.first}"))
            }

            if (path != null && path[0] != '/') fc.initialDirectory = File(path)
            return fc.showOpenDialog(stage)?.path
        }

        fun getDirectory(stage: Stage, path: String): String {
            val dc = DirectoryChooser()
            if (path != "") dc.initialDirectory = File(path)
            return dc.showDialog(stage)?.path ?: ""
        }
    }
}