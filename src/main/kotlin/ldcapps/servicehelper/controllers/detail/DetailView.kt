package ldcapps.servicehelper.controllers.detail

import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.DatePicker
import javafx.scene.control.TableView
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.stage.Stage
import ldcapps.servicehelper.*
import ldcapps.servicehelper.styles.MainStyle
import liklibs.db.toLocalDate
import liklibs.db.toSQL
import tornadofx.*
import java.time.LocalDate

class DetailView : View() {

    companion object {
        lateinit var mainStage: Stage
    }

    private lateinit var table: TableView<Detail>
    private lateinit var cancelBtn: Button
    private lateinit var parseBtn: Button
    private lateinit var customerCb: ComboBox<String>
    private lateinit var datePicker: DatePicker

    override val root = form {
        addClass(MainStyle.mainWindow)

        mainStage = primaryStage

        details = loadDetails()
        details.sortBy { it.date }
        hbox(15) {
            paddingVertical = 10
            datePicker = datepicker {
                value = LocalDate.now()
                promptText = "Дата"
                isEditable = false
            }
            val carTf = textfield { promptText = "Авто" }
            val detailTf = textfield { promptText = "Деталь" }
            val priceTf = textfield { promptText = "Цена" }
            customerCb =
                combobox(values = pConfig.customers) {
                    value = ""
                    isEditable = true
                    promptText = "Заказчик"
                }
            button("+") {
                prefHeight = 30.0
                prefWidth = 30.0

                isDefaultButton = true
                action {
                    var dIndex = details.map { it.date }.lastIndexOf(datePicker.value)
                    if (dIndex == -1)
                        dIndex = details.size
                    else dIndex++
                    details.add(
                        dIndex,
                        Detail(
                            details.size + 1,
                            datePicker.value,
                            carTf.text,
                            detailTf.text,
                            priceTf.text.replace(",", ".").toDouble(),
                            customerCb.editor.text
                        )
                    )
                    saveDetails()
                    table.smartResize()
                    table.refresh()
                    if (!pConfig.customers.contains(customerCb.value))
                        pConfig.customers.add(customerCb.value)
                    toJSON(".config", pConfig)
                    detailTf.text = ""
                    priceTf.text = ""
                    customerCb.editor.text = ""
                }
            }
        }
        table = tableview(details) {
            prefHeight = 300.0
            setOnKeyReleased {
                if (selectionModel.selectedItem != null && it.isControlDown && it.code == KeyCode.DELETE) {
                    Dialogs.confirmation("Удалить деталь?") {
                        details.remove(selectionModel.selectedItem)
                        saveDetails()
                        table.refresh()
                    }
                }
            }
            isEditable = true
            readonlyColumn("#", Detail::detail) {
                setCellValueFactory { SimpleStringProperty((details.indexOf(it.value) + 1).toString()) }
            }
            column("Дата", Detail::dateProperty).makeEditable().apply { onEditCommit { saveDetails() } }
            column("Авто", Detail::carProperty).makeEditable().apply { onEditCommit { saveDetails() } }
            column("Деталь", Detail::detailProperty).makeEditable().apply { onEditCommit { saveDetails() } }
            column("Цена", Detail::priceProperty).makeEditable().apply { onEditCommit { saveDetails() } }
            column("Заказчик", Detail::customerProperty).makeEditable().apply { onEditCommit { saveDetails() } }
                .useComboBox(pConfig.customers.asObservable()).apply { onEditCommit { saveDetails() } }
            column("", Detail::type).makeEditable().cellFormat {
                if (tableRow != null && tableRow.item != null && tableRow.item.type != "1")
                    graphic = checkbox {
                        isSelected = rowItem.type == "0"
                        action {
                            isSelected = !isSelected
                            if (tableRow.item != null && tableRow.item.type != "1")
                                if (!isSelected) {
                                    isSelected = true
                                    rowItem.type = "0"
                                    table.refresh()
                                } else Dialogs.confirmation("Убрать галочку?") {
                                    isSelected = false
                                    rowItem.type = ""
                                }
                            refresh()
                            items = details
                            saveDetails()
                        }
                    }
            }
            readonlyColumn("", Detail::type).cellFormat {
                style {
                    when (item) {
                        null -> backgroundColor.elements.clear()
                        "" -> backgroundColor += c("fff", 0.0)
                        "0" -> backgroundColor += c("fff", 0.0)
                        "1" -> backgroundColor += Color.GRAY
                        else -> backgroundColor += c(item!!)
                    }
                }
                setOnMouseClicked {
                    if (tableRow.item != null) {
                        if (tableRow.item.type != "1") {
                            fun plusItem() {
                                tableRow.item.type = when {
                                    tableRow.item.type == "" -> pConfig.colors.firstOrNull() ?: ""
                                    pConfig.colors.indexOf(tableRow.item.type) + 1 < pConfig.colors.size -> pConfig.colors[pConfig.colors.indexOf(
                                        tableRow.item.type
                                    ) + 1]
                                    else -> ""
                                }
                                if (tableRow.item.type == "")
                                    style { backgroundColor.elements.clear() }
                                else
                                    style { backgroundColor += c(tableRow.item.type) }
                                saveDetails()
                                refresh()
                            }
                            if (tableRow.item.type == "0")
                                Dialogs.confirmation("Убрать галочку?") { plusItem() } else plusItem()
                        }
                    } else {
                        tableView.selectionModel.clearSelection()
                        style { backgroundColor.elements.clear() }
                    }
                }
            }
            smartResize()
        }
        fun Button.colorButton(color: String) = apply {
            minWidth = 65.0
            style { backgroundColor += c(color) }
            action {
                SelectedDetailView(color).openWindow()
            }
            onRightClick {
                pConfig.colors.remove(color)
                isVisible = false
                toJSON(".config", pConfig)
            }
        }
        hbox(15) {
            paddingVertical = 10
            colorpicker {
                setOnAction {
                    this@hbox.button {
                        colorButton(this@colorpicker.value.toString())
                        pConfig.colors.add(this@colorpicker.value.toString())
                        toJSON(".config", pConfig)
                    }
                }
            }
            pConfig.colors.forEach {
                button {
                    colorButton(it)
                }
            }
        }
        hbox(15) {
            paddingVertical = 10
            button("✔") {
                minWidth = 65.0
                action {
                    SelectedDetailView("0").openWindow()
                }
            }
            button {
                minWidth = 65.0
                action {
                    SelectedDetailView("").openWindow()
                }
            }
            button("Сохраненные") {
                action { SavedDetailView().openWindow() }
            }
        }
        val area = textarea {
            promptText =
                "Авто (Оставить пустым для добавления позиций без авто)\n\rДеталь1 Цена1\n\rДеталь2 Цена2\n\rДеталь3 Цена3\n\rАвто (Оставить пустым для добавления позиций без авто)\n\rДеталь1 Цена1\n\rДеталь2 Цена2\n\rДеталь3 Цена3\n\r..."
        }
        hbox(15) {
            var parsedText = ""
            paddingVertical = 10
            val arr = mutableListOf<Triple<String, String, Double>>()
            val confirmBtn = button("Подтвердить") {
                isVisible = false
                action {
                    details.addAll(
                        arr.map {
                            Detail(
                                details.size + 1,
                                datePicker.value,
                                it.first,
                                it.second,
                                it.third,
                                customerCb.editor.text
                            )
                        })
                    saveDetails()
                    cancelBtn.isVisible = false
                    isVisible = false
                    area.isEditable = true
                    area.text = ""
                    parseBtn.isVisible = true
                }
            }
            cancelBtn = button("Отмена") {
                isVisible = false
                action {
                    confirmBtn.isVisible = false
                    isVisible = false
                    area.isEditable = true
                    area.text = parsedText
                    parseBtn.isVisible = true
                }
            }
            parseBtn = button("Парсинг") {
                action {
                    parsedText = area.text.replace(",", ".")
                    area.text = ""
                    var car = ""
                    arr.clear()
                    Regex("[\\n]{3,}").replace(parsedText, "\n\n").split("\n").map {
                        Regex("^\\s+|\\s+$").replace(Regex("\\s+").replace(it, " "), "")
                    }.forEach {
                        if (it.substringAfterLast(" ").isDouble()) {
                            area.text += "$car: ${it.substringBeforeLast(" ")}:  ${it.substringAfterLast(" ")}\n"
                            arr.add(Triple(car, it.substringBeforeLast(" "), it.substringAfterLast(" ").toDouble()))
                        } else car = it
                    }
                    area.isEditable = false
                    confirmBtn.isVisible = true
                    cancelBtn.isVisible = true
                    isVisible = false
                }
            }
        }
    }

    private fun saveDetails() =
        toJSON(".details", details.map { DetailND(it.date.toSQL(), it.car, it.detail, it.price, it.customer, it.type) })

    private fun loadDetails(): ObservableList<Detail> {
        return arrFromJSON<DetailND>(".details").mapIndexed { i, it ->
            Detail(
                i + 1,
                it.date.toLocalDate(),
                it.car,
                it.detail,
                it.price,
                it.customer,
                it.type
            )
        }.asObservable()
    }
}
