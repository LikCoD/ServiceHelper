package ldcapps.servicehelper.controllers.detail

import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.CheckBox
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import ldcapps.servicehelper.*
import liklibs.db.toSQL
import tornadofx.*
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SelectedDetailView(private var type: String) : View() {

    private lateinit var stayCb: CheckBox

    override val root = form {
        val sDetails = details.filter { it.type == type }.sortedBy { it.date }
        val paperWidth = 420.0
        val pages = mutableListOf<HBox>()

        lateinit var hb: HBox

        vbox {
            scrollpane {
                prefHeight = 594.0
                prefWidth = paperWidth
                maxHeight = 594.0
                hb = hbox {
                    pages.add(hbox {
                        style {
                            borderColor += CssBox(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK)
                        }
                        maxWidth = paperWidth
                        maxHeight = 594.0
                        minWidth = paperWidth
                        minHeight = 594.0
                    })
                    var colCount = 15
                    var vCount = 0
                    val date = mutableListOf<LocalDate>()
                    sDetails.forEach {
                        if (!date.contains(it.date))
                            date.add(it.date)
                    }
                    var colBox = pages.last().vbox()

                    fun newCol() {
                        colCount = 0
                        vCount++
                        if (vCount == 3) {
                            vCount = 0
                            pages.add(hbox {
                                style {
                                    borderColor += CssBox(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK)
                                }
                                maxWidth = paperWidth
                                maxHeight = 594.0
                                minWidth = paperWidth
                                minHeight = 594.0
                            })
                        } else
                            pages.last().separator(Orientation.VERTICAL)
                        colBox = pages.last().vbox { }
                    }

                    colBox.label("Всего: ${String.format("%.2f", sDetails.sumOf { it.price })}") {
                        font = Font.font(null, FontWeight.BOLD, 10.0)
                    }
                    date.forEach { ld ->
                        val fDetails = sDetails.filter { it.date == ld }
                        if (colCount + 13 > 594)
                            newCol()
                        colBox.label(
                            "${DateTimeFormatter.ofPattern("MM.dd.yy").format(ld)}: ${
                                String.format(
                                    "%.2f",
                                    fDetails.sumOf { it.price })
                            }"
                        ) {
                            colCount += 13
                            prefWidth = paperWidth / 3
                            alignment = Pos.CENTER
                            font = Font.font(null, FontWeight.BOLD, 9.0)
                        }
                        val cars = mutableListOf<String>()
                        fDetails.forEach {
                            if (!cars.contains(it.car))
                                cars.add(it.car)
                        }
                        cars.forEach { c ->
                            val cDetails = fDetails.filter { it.car == c }
                            if (colCount + 11 > 594)
                                newCol()
                            colBox.label("$c: ${String.format("%.2f", cDetails.sumOf { it.price })}") {
                                colCount += 11
                                font = Font.font(null, FontWeight.BOLD, 7.0)
                                prefWidth = paperWidth / 3
                                alignment = Pos.CENTER
                            }
                            cDetails.forEachIndexed { i, it ->
                                if (colCount + 11 > 594)
                                    newCol()
                                colBox.hbox {
                                    colCount += 11
                                    label("${i + 1}") {
                                        prefWidth = 20.0
                                        font = Font.font(7.0)
                                    }
                                    label(it.detail) {
                                        prefWidth = ((paperWidth - 82) / 3.0) / 5 * 4
                                        font = Font.font(7.0)
                                    }
                                    label(String.format("%.2f", it.price)) {
                                        prefWidth = ((paperWidth - 82) / 3.0) / 5
                                        alignment = Pos.CENTER_RIGHT
                                        font = Font.font(7.0)
                                    }
                                }
                            }
                        }
                    }
                    maxHeight = 594.0
                    minHeight = 594.0
                }
            }
            hbox(10) {
                fun save() {
                    val savedDetails = arrFromJSON<SavedDetails>(".saveddetails")
                    savedDetails.add(
                        0, SavedDetails(savedDetails.size, details.filter { it.type == type }.map {
                            DetailND(
                                it.date.toSQL(),
                                it.car,
                                it.detail,
                                it.price,
                                it.customer,
                                it.type
                            )
                        }
                        ))
                    toJSON(File(".saveddetails"), savedDetails.toList())
                }

                fun delete() {
                    details.removeIf { it.type == type }
                    toJSON(
                        File(".details"),
                        details.map { DetailND(it.date.toSQL(), it.car, it.detail, it.price, it.customer, it.type) })
                }

                fun stay() {
                    details.map { it.apply { if (type == this@SelectedDetailView.type) type = "1" } }
                    toJSON(
                        File(".details"),
                        details.map { DetailND(it.date.toSQL(), it.car, it.detail, it.price, it.customer, it.type) })
                }

                val printCb = checkbox("Печать") { isSelected = true }
                val saveCb = checkbox("Сохранить") { isSelected = true }
                val delCb = checkbox("Удалить") {
                    isSelected = true
                    action {
                        if (isSelected)
                            stayCb.isSelected = false
                    }
                }


                stayCb = checkbox("Запретить редактирование") {
                    action {
                        if (isSelected)
                            delCb.isSelected = false
                    }
                }

                button("Подтвердить") {
                    action {
                        if (printCb.isSelected) PrintView(hb).openModal()
                        if (saveCb.isSelected) save()
                        if (delCb.isSelected) delete()
                        if (stayCb.isSelected) stay()
                        close()
                    }
                }
            }
        }
    }
}
