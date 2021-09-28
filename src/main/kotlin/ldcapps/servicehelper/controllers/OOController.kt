package ldcapps.servicehelper.controllers

import javafx.fxml.Initializable
import javafx.print.PageOrientation
import javafx.scene.control.*
import javafx.scene.control.TableColumn.CellEditEvent
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import ldcapps.servicehelper.*
import ldcapps.servicehelper.controllers.tools.AddCar
import ldcapps.servicehelper.controllers.tools.Panes
import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.db.DataClasses.Companion.cars
import ldcapps.servicehelper.db.DataClasses.Companion.companies
import ldcapps.servicehelper.db.DataClasses.Companion.individuals
import ldcapps.servicehelper.db.DataClasses.Companion.reports
import ldcapps.servicehelper.db.DataClasses.Companion.user
import ldclibs.javafx.controls.*
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.net.URL
import java.util.*

class OOController : Initializable {
    lateinit var mainVBox: VBox
    lateinit var firstHBox: HBox
    lateinit var positionsVBox: VBox
    lateinit var createActBtn: Button
    lateinit var confirmBtn: Button
    lateinit var cancelBtn: Button
    lateinit var workNameTf: AutoCompletedTextField<Data.Hint>
    lateinit var workPriceTf: PriceTextField
    lateinit var workExecutorsCb: ComboBox<String>
    lateinit var excelWorkBtn: Button
    lateinit var dpcNameTf: AutoCompletedTextField<Data.Hint>
    lateinit var dpcCountTf: IntTextField
    lateinit var dpcPriceTf: PriceTextField
    lateinit var dpcSumTf: PriceTextField
    lateinit var dpcUnitCb: ComboBox<String>
    lateinit var dpcStateCb: ComboBox<String>
    lateinit var dpcExecutorsCb: ComboBox<String>
    lateinit var excelDPCBtn: Button
    lateinit var dfcNameTf: AutoCompletedTextField<Data.Hint>
    lateinit var dfcCountTf: IntTextField
    lateinit var dfcUnitCb: ComboBox<String>
    lateinit var dfcStateCb: ComboBox<String>
    lateinit var dfcExecutorsCb: ComboBox<String>
    lateinit var excelDFCBtn: Button
    lateinit var totalWorkPriceTf: PriceTextField
    lateinit var totalDPCPriceTf: PriceTextField
    lateinit var totalPriceTf: PriceTextField
    lateinit var carNumberTf: AutoCompletedTextField<String>
    lateinit var carMileageTf: IntTextField
    lateinit var ooNumberTf: IntTextField
    lateinit var registrationDp: DatePicker
    lateinit var executionDp: DatePicker
    lateinit var ownerTf: MyTextField
    lateinit var previewBtn: Button
    lateinit var ooAndBillScroll: ScrollPane
    lateinit var ooAndBillAp: AnchorPane
    lateinit var ooAp: AnchorPane
    lateinit var ooNumberTx: Label
    lateinit var ooToBillTx: Label
    lateinit var executor1Tx: Text
    lateinit var executorPA1Tx: Label
    lateinit var customer1Tx: Text
    lateinit var customerAddress: Text
    lateinit var customerPA1Tx: Text
    lateinit var customerPRNTx: Text
    lateinit var ownerTx: Text
    lateinit var executor2Tx: Text
    lateinit var registrationDateTx: Label
    lateinit var executionDate1Tx: Label
    lateinit var executionDate2Tx: Label
    lateinit var cashTx: Label
    lateinit var carModelLb: Label
    lateinit var carMileageLb: Label
    lateinit var carEngineLb: Label
    lateinit var carYearLb: Label
    lateinit var carVINLb: Label
    lateinit var carNumberLb: Label
    lateinit var workTable: TableView<Work>
    lateinit var workNumberCol: Column<Work>
    lateinit var workNameCol: Column<Work>
    lateinit var workHourNormCol: Column<Work>
    lateinit var workPriceCol: Column<Work>
    lateinit var workExecutorsCol: Column<Work>
    lateinit var dpcTable: TableView<DPC>
    lateinit var dpcNumberCol: Column<DPC>
    lateinit var dpcDetailCol: Column<DPC>
    lateinit var dpcUnitCol: Column<DPC>
    lateinit var dpcCountCol: Column<DPC>
    lateinit var dpcStateCol: Column<DPC>
    lateinit var dpcPriceCol: Column<DPC>
    lateinit var dpcSumCol: Column<DPC>
    lateinit var dpcExecutorsCol: Column<DPC>
    lateinit var dfcTable: TableView<DFC>
    lateinit var dfcNumberCol: Column<DFC>
    lateinit var dfcDetailCol: Column<DFC>
    lateinit var dfcUnitCol: Column<DFC>
    lateinit var dfcCountCol: Column<DFC>
    lateinit var dfcStateCol: Column<DFC>
    lateinit var dfcExecutorsCol: Column<DFC>
    lateinit var ooFooterAp: AnchorPane
    lateinit var workPrice1Tx: PriceLabel
    lateinit var detailPrice1Tx: PriceLabel
    lateinit var totalPrice1Tx: PriceLabel
    lateinit var startPriceTx: Label
    lateinit var workPrice2Tx: PriceLabel
    lateinit var detailPrice2Tx: PriceLabel
    lateinit var totalPrice2Tx: PriceLabel
    lateinit var vatTx: Label
    lateinit var vatPriceTx: Label
    lateinit var totalPriceWithVAT1Tx: PriceLabel
    lateinit var endPriceTx: Label
    lateinit var executionDate3Tx: Text
    lateinit var customer2Tx: Label
    lateinit var executor3Tx: Text
    lateinit var executionDate4Tx: Text
    lateinit var executor4Tx: Text
    lateinit var executionDate5Tx: Text
    lateinit var customer4Tx: Label
    lateinit var customer3Tx: Label
    lateinit var executor5Tx: Label
    lateinit var billAp: AnchorPane
    lateinit var executor6Tx: Text
    lateinit var executorPA2Tx: Label
    lateinit var billNumberTx: Label
    lateinit var contractDateTx: Label
    lateinit var customer5Tx: Text
    lateinit var customerPA2Tx: Label
    lateinit var billTable: TableView<Int>
    lateinit var billNumberCol: Column<Int>
    lateinit var billNameCol: Column<Int>
    lateinit var billUnitCol: Column<Int>
    lateinit var billCountCol: Column<Int>
    lateinit var billPriceCol: Column<Int>
    lateinit var billSumCol: Column<Int>
    lateinit var billVatCol: Column<Int>
    lateinit var billVatPriceCol: Column<Int>
    lateinit var billTotalPriceWithVatCol: Column<Int>
    lateinit var billFooterAp: AnchorPane
    lateinit var totalPrice3Tx: Text
    lateinit var totalPriceWithVAT2Tx: Text
    lateinit var executor7Tx: Text
    lateinit var customer6Tx: Text
    lateinit var executor8Tx: Text

    private var ooAndBill = OOAndBill()

    private var action = 0
    private var path = ""

    private var cash = false

    private var billPos = mutableListOf<Int>()


    private lateinit var worksHint: List<Data.Hint>
    private lateinit var dpcsHint: List<Data.Hint>
    private lateinit var dfcsHint: List<Data.Hint>

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        workNameTf.getString = { it.name }
        dpcNameTf.getString = { "${it.name} * ${it.count} (${it.state})" }
        dfcNameTf.getString = { "${it.name} * ${it.count} (${it.state})" }

        excelWorkBtn.setOnAction { _ ->
            Dialogs.getFile(MainController.stage, null, "xlsx" to "Excel")?.let { path ->
                XSSFWorkbook(File(path)).use {
                    val sheet = it.getSheetAt(0)
                    val sequence = Dialogs.filter(0, "Позиция", "Цена", "Исполнитель")
                    var row: Row? = sheet.getRow(sequence.first)

                    while (row != null) {
                        ooAndBill.works.add(
                            Work(
                                getCellValue(row, sequence.second + sequence.third[0], ""),
                                getCellValue(row, sequence.second + sequence.third[1], 0.0),
                                getCellValue(row, sequence.second + sequence.third[0], user.standardWorker)
                            )
                        )

                        refresh()
                        row = sheet.getRow(row.rowNum + 1)
                    }
                }
            }
        }

        excelDPCBtn.setOnAction { _ ->
            Dialogs.getFile(MainController.stage, null, "xlsx" to "Excel")?.let { path ->
                XSSFWorkbook(File(path)).use {
                    val sheet = it.getSheetAt(0)
                    val sequence = Dialogs.filter(1, "Позиция", "Ед. изм.", "Кол-во", "Состояние", "Цена", "Принял")
                    var row: Row? = sheet.getRow(sequence.first)

                    while (row != null) {
                        ooAndBill.dpcs.add(
                            DPC(
                                getCellValue(row!!, sequence.second + sequence.third[0], ""),
                                getCellValue(row!!, sequence.second + sequence.third[1], user.standardUnit),
                                getCellValue(row!!, sequence.second + sequence.third[2], 1),
                                getCellValue(row!!, sequence.second + sequence.third[3], user.standardState),
                                getCellValue(row!!, sequence.second + sequence.third[4], 0.0),
                                getCellValue(row!!, sequence.second + sequence.third[5], user.standardWorker),
                            )
                        )

                        refresh()
                        row = sheet.getRow(row!!.rowNum + 1)
                    }
                }
            }
        }

        excelDFCBtn.setOnAction { _ ->
            Dialogs.getFile(MainController.stage, null, "xlsx" to "Excel")?.let { path ->
                XSSFWorkbook(File(path)).use {
                    val sheet = it.getSheetAt(0)
                    val sequence = Dialogs.filter(2, "Позиция", "Ед. изм.", "Кол-во", "Состояние", "Принял")
                    var row: Row? = sheet.getRow(sequence.first)

                    while (row != null) {
                        ooAndBill.dfcs.add(
                            DFC(
                                getCellValue(row!!, sequence.second + sequence.third[0], ""),
                                getCellValue(row!!, sequence.second + sequence.third[1], user.standardUnit),
                                getCellValue(row!!, sequence.second + sequence.third[2], 1),
                                getCellValue(row!!, sequence.second + sequence.third[3], user.standardState),
                                getCellValue(row!!, sequence.second + sequence.third[4], user.standardWorker),
                            )
                        )

                        refresh()
                        row = sheet.getRow(row!!.rowNum + 1)
                    }
                }
            }
        }

        workNameTf.onAutoCompleted = { work ->
            workPriceTf.text = work.price.toString()
            workPriceTf.requestFocus()
        }

        dpcNameTf.onAutoCompleted = { dpc ->
            dpcPriceTf.text = dpc.price.toString()
            dpcUnitCb.value = dpc.unit
            dpcStateCb.value = dpc.state
            dpcPriceTf.requestFocus()
        }

        dfcNameTf.onAutoCompleted = { dfc ->
            dfcUnitCb.value = dfc.unit
            dfcStateCb.value = dfc.state
        }

        carNumberTf.items = cars.map { it.keyNum }

        createActBtn.setOnAction {
            Dialogs.confirmation("При создании акта ЗН сохранится автоматически") {
                action = 0
                confirmBtn.fire()
                Windows.act()?.initOO(ooAndBill, path)
            }
        }

        cancelBtn.setOnAction {
            when (action) {
                0 -> Dialogs.confirmation("Подтвердите выход") { MainController.closeSelectedTab() }
                4 -> {
                    registrationDp.value = ooAndBill.registrationDate.localDate
                    executionDp.value = ooAndBill.executionDate.localDate
                    carMileageTf.text = carMileageLb.text
                }
            }
            confirmBtn.text = "Подтвердить"
            action = 0
        }

        confirmBtn.setOnAction {
            when (action) {
                0 -> {
                    if (ooAndBill.car == null) return@setOnAction
                    workTable.selectionModel.clearSelection()
                    dpcTable.selectionModel.clearSelection()
                    dfcTable.selectionModel.clearSelection()
                    billTable.selectionModel.clearSelection()

                    if (path == "")
                        path = "${if (cash) "Безнал" else "Нал"}\\Заказ-Наряд №${ooAndBill.number} от " +
                                "${ooAndBill.registrationDate} от ${ooAndBill.customer}.${if (cash) "oo" else "oab"}"

                    path = Regex("[/*?\"<>|]").replace(path, "")

                    toJSON(File(settings.oosLocate, path), ooAndBill)

                    val report = DataClasses.Report(
                        user.name,
                        ooAndBill.number,
                        if (ooAndBill.customerPA == null) 0 else 1,
                        ooAndBill.abbreviatedCompanyName,
                        ooAndBill.customer,
                        ooAndBill.car!!.owner,
                        ooAndBill.car!!.number,
                        ooAndBill.carMileage,
                        ooAndBill.registrationDate,
                        ooAndBill.executionDate,
                        ooAndBill.hourNorm,
                        roundPrice(ooAndBill.works.sumOf { it.price }),
                        roundPrice(ooAndBill.dpcs.sumOf { it.price * it.count }),
                        ooAndBill.vat,
                        ooAndBill.works.size,
                        ooAndBill.dpcs.size,
                        ooAndBill.dfcs.size
                    )

                    reports.removeIf { it.number == ooAndBill.number && it.carNumber == ooAndBill.car!!.number }
                    reports.add(report)

                    fun MutableSet<Data.Hint>.checkHint(hint: Data.Hint) {
                        val oldHint = find { h -> h.carModel == ooAndBill.car?.model && h.name == hint.name }

                        if (oldHint != null) {
                            if (oldHint.price != hint.price) oldHint.price = hint.price
                            if (oldHint.count != hint.count) oldHint.count = hint.count
                            if (oldHint.state != hint.state) oldHint.state = hint.state
                            if (oldHint.unit != hint.unit) oldHint.unit = hint.unit
                        } else add(hint)
                    }

                    ooAndBill.works.forEach {
                        val hint = Data.Hint(ooAndBill.car!!.model, it.name, it.price)
                        data.works.checkHint(hint)
                    }
                    ooAndBill.dpcs.forEach {
                        val hint = Data.Hint(ooAndBill.car!!.model, it.name, it.price, it.count, it.state, it.unit)
                        data.works.checkHint(hint)
                    }
                    ooAndBill.dfcs.forEach {
                        val hint = Data.Hint(ooAndBill.car!!.model, it.name, null, it.count, it.state, it.unit)
                        data.works.checkHint(hint)
                    }

                    toJSON(".data", data)

                    Dialogs.confirmation("Заказ-Наряд №${ooAndBill.number} успешно создан и находится по пути:\n$path\nРаспечатать его?") {
                        ooAndBill.customerPA?.let {
                            Dialogs.print(MainController.stage, PageOrientation.PORTRAIT, ooAp, billAp)
                        } ?: Dialogs.print(MainController.stage, PageOrientation.PORTRAIT, ooAp)
                    }
                }
                1 -> {
                    if (!isNotNull(workNameTf, workPriceTf, workPriceTf)) return@setOnAction

                    val work = Work(
                        workNameTf.text,
                        workPriceTf.text.toDouble(),
                        workExecutorsCb.value
                    )
                    ooAndBill.works.add(work)
                    refresh()

                    workNameTf.text = ""
                    workPriceTf.text = ""
                    workExecutorsCb.value = user.standardWorker
                }
                2 -> {
                    if (!isNotNull(dpcNameTf, dpcCountTf, dpcUnitCb, dpcStateCb, dpcPriceTf)) return@setOnAction

                    val dpc = DPC(
                        dpcNameTf.text,
                        dpcUnitCb.value,
                        dpcCountTf.text.toInt(),
                        dpcStateCb.value,
                        dpcPriceTf.text.toDouble(),
                        dpcExecutorsCb.value
                    )
                    ooAndBill.dpcs.add(dpc)
                    refresh()

                    dpcNameTf.text = ""
                    dpcCountTf.text = ""
                    dpcPriceTf.text = ""
                    dpcSumTf.text = ""
                    dpcUnitCb.value = user.standardUnit
                    dpcStateCb.value = user.standardState
                    dpcExecutorsCb.value = user.standardWorker
                }
                3 -> {
                    if (!isNotNull(dfcNameTf, dfcCountTf, dfcUnitCb, dfcStateCb)) return@setOnAction

                    val dfc = DFC(
                        dfcNameTf.text,
                        dfcUnitCb.value,
                        dfcCountTf.text.toInt(),
                        dfcStateCb.value,
                        dfcExecutorsCb.value
                    )
                    ooAndBill.dfcs.add(dfc)
                    refresh()

                    dfcNameTf.text = ""
                    dfcCountTf.text = ""
                    dfcUnitCb.value = user.standardUnit
                    dfcStateCb.value = user.standardState
                    dfcExecutorsCb.value = user.standardWorker
                }
                4 -> {
                    ooAndBill.executionDate = DataClasses.Date(executionDp.value)
                    ooAndBill.registrationDate = DataClasses.Date(registrationDp.value)
                    ooAndBill.carMileage = carMileageTf.text.toInt()
                    ooAndBill.number = ooNumberTf.text.toInt()

                    updateInfo()
                }
                5 -> {
                    cars.find { it.keyNum == carNumberTf.text }?.let { initCar(it) }
                        ?: Dialogs.confirmation("Данного гос. номера авто нет в БД, но вы можите добавить его в меню \"Инструменты\"") {
                            Panes.ADD_CAR.show<AddCar>(Windows.tools()!!.addCarTb).keyTf.text = carNumberTf.text
                        }
                }
            }
            confirmBtn.text = "Подтвердить"
            action = 0
        }

        previewBtn.setOnAction {
            ooAndBillScroll.isVisible = !ooAndBillScroll.isVisible

            if (ooAndBillScroll.isVisible) {
                firstHBox.children.add(1, positionsVBox)
                MainController.changeStageSize(FXML.OO)
            } else {
                mainVBox.children.add(positionsVBox)
                MainController.changeStageSize(FXML.OOCollapsed)
            }
        }

        workExecutorsCb.items = user.workers.toFXList()
        dfcExecutorsCb.items = user.workers.toFXList()
        dpcExecutorsCb.items = user.workers.toFXList()
        workExecutorsCb.value = user.standardWorker
        dfcUnitCb.value = user.standardUnit
        dfcStateCb.value = user.standardState
        dfcExecutorsCb.value = user.standardWorker
        dpcUnitCb.value = user.standardUnit
        dpcStateCb.value = user.standardState
        dpcExecutorsCb.value = user.standardWorker

        workNumberCol.setValueFactory { ooAndBill.works.indexOf(it) + 1 }
        workNameCol.setValueFactory(Work::name.name)
        workHourNormCol.setPriceValueFactory { it.price / ooAndBill.hourNorm }
        workPriceCol.setPriceValueFactory { it.price }
        workExecutorsCol.setValueFactory(Work::executor.name)
        dpcNumberCol.setValueFactory { ooAndBill.dpcs.indexOf(it) + 1 }
        dpcDetailCol.setValueFactory(DPC::name.name)
        dpcUnitCol.setValueFactory { if (it.unit == "ш") "шт." else "л." }
        dpcCountCol.setValueFactory { it.count }
        dpcStateCol.setValueFactory { if (it.state == "н") "новая" else "б/у" }
        dpcPriceCol.setPriceValueFactory { it.price }
        dpcSumCol.setPriceValueFactory { it.count * it.price }
        dpcExecutorsCol.setValueFactory(DPC::executor.name)
        dfcNumberCol.setValueFactory { ooAndBill.dfcs.indexOf(it) + 1 }
        dfcDetailCol.setValueFactory(DFC::name.name)
        dfcUnitCol.setValueFactory { if (it.unit == "ш") "шт." else "л." }
        dfcCountCol.setValueFactory { it.count }
        dfcStateCol.setValueFactory { if (it.state == "н") "новая" else "б/у" }
        dfcExecutorsCol.setValueFactory(DFC::executor.name)
        billNumberCol.setValueFactory { it + 1 }
        billNameCol.setValueFactory {
            if (it < ooAndBill.works.size) ooAndBill.works[it].name else ooAndBill.dpcs[it - ooAndBill.works.size].name
        }
        billUnitCol.setValueFactory {
            if (it < ooAndBill.works.size) "н.ч" else if (ooAndBill.dpcs[it - ooAndBill.works.size].unit == "ш") "шт." else "л."
        }
        billCountCol.setPriceValueFactory {
            if (it < ooAndBill.works.size) ooAndBill.works[it].price / ooAndBill.hourNorm
            else ooAndBill.dpcs[it - ooAndBill.works.size].count.toDouble()
        }
        billPriceCol.setPriceValueFactory {
            if (it < ooAndBill.works.size) ooAndBill.hourNorm
            else ooAndBill.dpcs[it - ooAndBill.works.size].price
        }
        billSumCol.setPriceValueFactory {
            if (it < ooAndBill.works.size) ooAndBill.works[it].price
            else (ooAndBill.dpcs[it - ooAndBill.works.size].price * ooAndBill.dpcs[it - ooAndBill.works.size].count)
        }
        billVatCol.setValueFactory { ooAndBill.vat?.toString() ?: "Без налога" }
        billVatPriceCol.setPriceValueFactory {
            when {
                ooAndBill.vat == null -> null
                it < ooAndBill.works.size -> roundPrice(ooAndBill.vat!!.div(100) * ooAndBill.works[it].price)
                else -> ooAndBill.dpcs[it - ooAndBill.works.size].price * (ooAndBill.vat!!.div(100))
            }
        }
        billTotalPriceWithVatCol.setPriceValueFactory {
            if (it < ooAndBill.works.size) ooAndBill.works[it].price * (ooAndBill.vat?.div(100)?.plus(1) ?: 1.0)
            else ooAndBill.dpcs[it - ooAndBill.works.size].price * (ooAndBill.vat?.div(100)?.plus(1) ?: 1.0)
        }

        workNameCol.setTextFieldCellFactory()
        workPriceCol.setPriceTextFieldCellFactory()
        workExecutorsCol.setComboBoxCellFactory(*user.workers.toTypedArray())
        dfcDetailCol.setTextFieldCellFactory()
        dfcCountCol.setIntTextFieldCellFactory()
        dfcStateCol.setComboBoxCellFactory("н", "б")
        dfcUnitCol.setComboBoxCellFactory("ш", "л")
        dfcExecutorsCol.setComboBoxCellFactory(*user.workers.toTypedArray())
        dpcDetailCol.setTextFieldCellFactory()
        dpcCountCol.setIntTextFieldCellFactory()
        dpcPriceCol.setPriceTextFieldCellFactory()
        dpcSumCol.setPriceTextFieldCellFactory()
        dpcStateCol.setComboBoxCellFactory("н", "б")
        dpcUnitCol.setComboBoxCellFactory("ш", "л")
        dpcExecutorsCol.setComboBoxCellFactory(*user.workers.toTypedArray())
    }

    private fun updateInfo() {
        ooNumberTf.text = ooAndBill.number.toString()
        registrationDateTx.text = ooAndBill.registrationDate.value
        executionDate1Tx.text = ooAndBill.executionDate.value
        executionDate2Tx.text = ooAndBill.executionDate.value
        executionDate3Tx.text = ooAndBill.executionDate.value
        executionDate4Tx.text = ooAndBill.executionDate.value
        executionDate5Tx.text = ooAndBill.executionDate.value
        carMileageLb.text = ooAndBill.carMileage?.toString()

        MainController.selectedTab.text = "${if (cash) "Н" else "Б"} ЗН №${ooAndBill.number}, ${ooAndBill.car?.model}"

        ooNumberTx.text = "ЗАКАЗ-НАРЯД №${ooAndBill.number}"
        if (!cash) billNumberTx.text = "СЧЕТ №${ooAndBill.number} от ${ooAndBill.executionDate.value}"
    }

    fun fill(filledOOAndBill: OOAndBill, path: String = "") {
        this.path = path
        ooAndBill = filledOOAndBill
        refresh()

        registrationDp.value = ooAndBill.registrationDate.localDate
        executionDp.value = ooAndBill.executionDate.localDate
        updateInfo()

        initCar()
    }

    private fun initCar(car: DataClasses.Car = ooAndBill.car!!) {
        ooAndBill.car = car

        companies.find { c -> c.company == ooAndBill.customer }?.let { c ->
            ooAndBill.customerAddress = c.address
            ooAndBill.customerPA = c.pa
            ooAndBill.customerBank = c.bank
            ooAndBill.customerBIK = c.bik
            ooAndBill.customerPRN = c.prn
            ooAndBill.customerContractDate = c.contractDate
        } ?: individuals.find { i -> i.individual == ooAndBill.customer }?.let { i ->
            ooAndBill.customerAddress = i.address
        }

        worksHint = data.works.filter { it.carModel == car.model }
        dpcsHint = data.dpcs.filter { it.carModel == car.model }
        dfcsHint = data.dfcs.filter { it.carModel == car.model }

        workNameTf.items = worksHint
        dpcNameTf.items = dpcsHint
        dfcNameTf.items = dfcsHint

        carMileageTf.text = ooAndBill.carMileage?.toString()
        carNumberTf.text = car.number
        carModelLb.text = car.model
        carEngineLb.text = car.engine.toString()
        carYearLb.text = car.year.toString()
        carVINLb.text = car.vin
        carNumberLb.text = car.number
        ownerTf.text = car.owner
        ownerTx.text = "Владелец: ${car.owner}"
        customerAddress.text = "Адрес: ${ooAndBill.customerAddress}"
        ooAndBill.customerPA?.let {
            cashTx.text = "Безналичный"
            cash = false
            customerPA1Tx.text = "Р/с: $it в ${ooAndBill.customerBank} БИК ${ooAndBill.customerBIK}"
            customerPA2Tx.text =
                "Плательщик: ${ooAndBill.customer}, Адрес: ${ooAndBill.customerAddress}, Р/с: ${ooAndBill.customerPA} в ${ooAndBill.customerBank}"
            customerPRNTx.text = "УНП - ${ooAndBill.customerPRN}"
            ooToBillTx.text =
                "Является актом выполненных работ к счету №${ooAndBill.number} от ${ooAndBill.executionDate.value}"
            executor7Tx.text = ooAndBill.companyName
            executor8Tx.text = ooAndBill.abbreviatedCompanyName
            executorPA2Tx.text =
                "Р/с - ${ooAndBill.companyPA} в ${ooAndBill.companyBank}, ${ooAndBill.companyAddress}, Адрес банка - ${ooAndBill.companyBankAddress}, " +
                        "БИК - ${ooAndBill.companyBIK}, УНП - ${ooAndBill.companyPRN}, " +
                        "Почт. адрес - ${ooAndBill.companyAddress}, Тел. - +${ooAndBill.companyPhone}"
            contractDateTx.text = "договор б/н от ${ooAndBill.customerContractDate}"
            customer5Tx.text = "Заказчик: ${ooAndBill.customer}"
            customer6Tx.text = ooAndBill.customer
            billAp.isVisible = true
        } ?: run {
            cashTx.text = "Наличный"
            customerPA1Tx.text = ""
            customerPRNTx.text = ""
            cash = true
            customerPA2Tx.text = "Плательщик: ${ooAndBill.customer}"
            billAp.isVisible = false
        }
        executor1Tx.text = ooAndBill.companyName
        executor2Tx.text = "Исполнитель: ${ooAndBill.companyName}"
        executor3Tx.text = ooAndBill.abbreviatedCompanyName
        executor4Tx.text = ooAndBill.abbreviatedCompanyName
        executor5Tx.text = ooAndBill.abbreviatedCompanyName
        executor6Tx.text = ooAndBill.companyName
        executorPA1Tx.text =
            "УНП - ${ooAndBill.companyPRN}, ${ooAndBill.companyAddress}, Р/с - ${ooAndBill.companyPA} в ${ooAndBill.companyBank}, " +
                    "БИК - ${ooAndBill.companyBIK}, Адрес банка - ${ooAndBill.companyBankAddress}"
        customer1Tx.text = ooAndBill.customer
        customer2Tx.text = ooAndBill.customer
        customer3Tx.text = ooAndBill.customer
        customer4Tx.text = ooAndBill.customer
    }

    private fun refresh() {
        workTable.items = ooAndBill.works.toFXList()
        workTable.refresh()
        dpcTable.items = ooAndBill.dpcs.toFXList()
        dpcTable.refresh()
        dfcTable.items = ooAndBill.dfcs.toFXList()
        dfcTable.refresh()
        billPos = mutableListOf()

        for (i in 0 until ooAndBill.works.size + ooAndBill.dpcs.size) billPos.add(i)

        billTable.items = billPos.toFXList()
        billTable.refresh()
        dpcTable.layoutY = 315.0 + ooAndBill.works.size * 20
        dfcTable.layoutY = 380.0 + (ooAndBill.works.size + ooAndBill.dpcs.size) * 20
        ooFooterAp.layoutY = 445.0 + (ooAndBill.works.size + ooAndBill.dpcs.size + ooAndBill.dfcs.size) * 20
        billFooterAp.layoutY = 250.0 + (billPos.size * 20)
        workTable.prefHeight = 57.0 + ooAndBill.works.size * 20
        dpcTable.prefHeight = 57.0 + ooAndBill.dpcs.size * 20
        dfcTable.prefHeight = 57.0 + ooAndBill.dfcs.size * 20
        billTable.prefHeight = 37.0 + billPos.size * 20

        val workPrice = ooAndBill.works.sumOf { it.price }
        val dpcPrice = ooAndBill.dpcs.sumOf { it.price * it.count }

        totalWorkPriceTf.text = workPrice.toString()
        totalDPCPriceTf.text = dpcPrice.toString()
        totalPriceTf.text = (workPrice + dpcPrice).toString()
        workPrice1Tx.value = workPrice
        detailPrice1Tx.value = dpcPrice
        totalPrice1Tx.value = workPrice + dpcPrice
        workPrice2Tx.value = workPrice
        detailPrice2Tx.value = dpcPrice
        totalPrice2Tx.value = workPrice + dpcPrice
        startPriceTx.text = numToStr(workPrice + dpcPrice)
        vatTx.text = ooAndBill.vat?.toString() ?: "Без налога"
        ooAndBill.vat?.let {
            vatPriceTx.text = roundPrice((workPrice + dpcPrice) * ooAndBill.vat!! / 100).toString()
            totalPriceWithVAT1Tx.value = (workPrice + dpcPrice) * (ooAndBill.vat!! / 100 + 1)
            endPriceTx.text = numToStr(((workPrice + dpcPrice) * (ooAndBill.vat!! / 100 + 1)))
            totalPrice3Tx.text =
                "Всего к оплате с НДС: ${numToStr((workPrice + dpcPrice) * (ooAndBill.vat!! / 100 + 1))}"
            totalPriceWithVAT2Tx.text =
                "В том числе НДС: ${numToStr((workPrice + dpcPrice) * (ooAndBill.vat!! / 100 + 1))}"
        } ?: run {
            vatPriceTx.text = "-"
            totalPriceWithVAT1Tx.value = workPrice + dpcPrice
            endPriceTx.text = numToStr(workPrice + dpcPrice)
            totalPrice3Tx.text = "Всего к оплате с НДС: ${numToStr(workPrice + dpcPrice)}"
            totalPriceWithVAT2Tx.text = "В том числе НДС: Без НДС"
        }
        var totalPos = ooAndBill.works.size + ooAndBill.dpcs.size + ooAndBill.dfcs.size - 19

        if (totalPos > 1) {
            ooAp.prefHeight = 1130.0 + totalPos * 20
            ooAndBillAp.prefHeight = 1130.0 + totalPos * 20
        }

        totalPos = ooAndBill.works.size + ooAndBill.dpcs.size - 34

        if (ooAndBill.works.size + ooAndBill.dpcs.size > 35)
            billAp.prefHeight = 1130.0 + totalPos * 20
    }

    fun onPositionsMouseClicked(mouseEvent: MouseEvent) {
        action = when {
            mouseEvent.source.toString().contains("work") -> {
                confirmBtn.text = "Добавить работу"
                1
            }
            mouseEvent.source.toString().contains("dpc") -> {
                confirmBtn.text = "Добавить дет. опл. зак."
                2
            }
            mouseEvent.source.toString().contains("dfc") -> {
                confirmBtn.text = "Добавить дет. прин. от зак."
                3
            }
            mouseEvent.source.toString().contains("carNumberTf") -> {
                confirmBtn.text = "Применить авто"
                5
            }
            else -> {
                confirmBtn.text = "Применить изменения"
                4
            }
        }
    }

    fun onWorkTableKeyPressed(event: KeyEvent) {
        if (event.code == KeyCode.DELETE && event.isControlDown) {
            workTable.selectionModel.selectedItem?.let {
                ooAndBill.works.remove(workTable.selectionModel.selectedItem)
                refresh()
            } ?: Dialogs.warning("Выберите позицию для удаления")
        }
    }

    fun onDPCTableKeyPressed(event: KeyEvent) {
        if (event.code == KeyCode.DELETE && event.isControlDown) {
            dpcTable.selectionModel.selectedItem?.let {
                ooAndBill.dpcs.remove(dpcTable.selectionModel.selectedItem)
                refresh()
            } ?: Dialogs.warning("Выберите позицию для удаления")
        }
    }

    fun onDFCTableKeyPressed(event: KeyEvent) {
        if (event.code == KeyCode.DELETE && event.isControlDown) {
            dfcTable.selectionModel.selectedItem?.let {
                ooAndBill.dfcs.remove(dfcTable.selectionModel.selectedItem)
                refresh()
            } ?: Dialogs.warning("Выберите позицию для удаления")
        }
    }

    fun changeWorkNameCellEvent(editEvent: CellEditEvent<Work, String>) {
        workTable.selectionModel.selectedItem.name = editEvent.newValue!!
        refresh()
    }

    fun changeWorkPriceCellEvent(editEvent: CellEditEvent<Work, String>) {
        totalWorkPriceTf.text = ooAndBill.works.sumOf { it.price }.toString()
        totalPriceTf.text =
            (ooAndBill.works.sumOf { it.price } + ooAndBill.dpcs.sumOf { it.price * it.count }).toString()
        workTable.selectionModel.selectedItem.price = roundPrice(editEvent.newValue.toDouble())
        workTable.refresh()
        refresh()
    }

    fun changeWorkExecutorCellEvent(editEvent: CellEditEvent<Work, String>) {
        workTable.selectionModel.selectedItem.executor = editEvent.newValue!!
        refresh()
    }

    fun changeDFCDetailCellEvent(editEvent: CellEditEvent<DFC, String>) {
        dfcTable.selectionModel.selectedItem.name = editEvent.newValue!!
        refresh()
    }

    fun changeDFCCountCellEvent(editEvent: CellEditEvent<DFC, String?>) {
        dfcTable.selectionModel.selectedItem.count = editEvent.newValue!!.toInt()
        refresh()
    }

    fun changeDFCUnitCellEvent(editEvent: CellEditEvent<DFC, String?>) {
        dfcTable.selectionModel.selectedItem.unit = editEvent.newValue!!
        refresh()
    }

    fun changeDFCStateCellEvent(editEvent: CellEditEvent<DFC, String?>) {
        dfcTable.selectionModel.selectedItem.state = editEvent.newValue!!
        refresh()
    }

    fun changeDFCExecutorCellEvent(editEvent: CellEditEvent<DFC, String?>) {
        dfcTable.selectionModel.selectedItem.executor = editEvent.newValue!!
        refresh()
    }

    fun changeDPCDetailCellEvent(editEvent: CellEditEvent<DPC, String?>) {
        dpcTable.selectionModel.selectedItem.name = editEvent.newValue!!
        refresh()
    }

    fun changeDPCCountCellEvent(editEvent: CellEditEvent<DPC, String?>) {
        dpcTable.selectionModel.selectedItem.count = editEvent.newValue!!.toInt()
        totalDPCPriceTf.text = ooAndBill.dpcs.sumOf { it.price }.toString()
        totalPriceTf.text =
            (ooAndBill.works.sumOf { it.price } + ooAndBill.dpcs.sumOf { it.price * it.count }).toString()
        dpcTable.refresh()
        refresh()
    }

    fun changeDPCPriceCellEvent(editEvent: CellEditEvent<DPC, String?>) {
        dpcTable.selectionModel.selectedItem.price = roundPrice(editEvent.newValue!!.toDouble())
        totalDPCPriceTf.text = ooAndBill.dpcs.sumOf { it.price * it.count }.toString()
        totalPriceTf.text =
            (ooAndBill.works.sumOf { it.price } + ooAndBill.dpcs.sumOf { it.price * it.count }).toString()
        dpcTable.refresh()
        refresh()
    }

    fun changeDPCSumCellEvent(editEvent: CellEditEvent<DPC, String?>) {
        totalDPCPriceTf.text = ooAndBill.dpcs.sumOf { it.price * it.count }.toString()
        totalPriceTf.text =
            (ooAndBill.works.sumOf { it.price } + ooAndBill.dpcs.sumOf { it.price * it.count }).toString()
        dpcTable.selectionModel.selectedItem.price =
            roundPrice(editEvent.newValue!!.toDouble() / dpcTable.selectionModel.selectedItem.count)
        dpcTable.refresh()
        refresh()
    }

    fun changeDPCUnitCellEvent(editEvent: CellEditEvent<DPC, String?>) {
        dpcTable.selectionModel.selectedItem.unit = editEvent.newValue!!
        refresh()
    }

    fun changeDPCStateCellEvent(editEvent: CellEditEvent<DPC, String?>) {
        dpcTable.selectionModel.selectedItem.state = editEvent.newValue!!
        refresh()
    }

    fun changeDPCExecutorCellEvent(editEvent: CellEditEvent<DPC, String?>) {
        dpcTable.selectionModel.selectedItem.executor = editEvent.newValue!!
        refresh()
    }

    class OOAndBill(
        var number: Int = 0,
        var registrationDate: DataClasses.Date = DataClasses.Date(),
        var executionDate: DataClasses.Date = DataClasses.Date(),
        var car: DataClasses.Car? = null,
        var carMileage: Int? = null,
        var customer: String = "",
        var customerAddress: String = "",
        var customerPA: String? = null,
        var customerBank: String? = null,
        var customerBIK: String? = null,
        var customerPRN: String? = null,
        var customerContractDate: String? = null,
        var vat: Double? = null,
        var hourNorm: Double = 0.0,
        var companyName: String = "",
        var abbreviatedCompanyName: String = "",
        var companyAddress: String = "",
        var companyPA: String = "",
        var companyBank: String = "",
        var companyBankAddress: String = "",
        var companyBIK: String = "",
        var companyPRN: String = "",
        var companyPhone: String? = null,
        var works: MutableList<Work> = mutableListOf(),
        var dpcs: MutableList<DPC> = mutableListOf(),
        var dfcs: MutableList<DFC> = mutableListOf(),
    )

    class Work(var name: String = "", var price: Double = 0.0, var executor: String = "")

    class DPC(
        var name: String = "", var unit: String = "", var count: Int = 0,
        var state: String = "", var price: Double = 0.0, var executor: String = "",
    )

    class DFC(
        var name: String = "", var unit: String = "", var count: Int = 0,
        var state: String = "", var executor: String = "",
    )

    private fun roundPrice(price: Double) = String.format("%.2f", price).replace(",", ".").toDouble()
}