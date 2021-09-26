package ldcapps.servicehelper.controllers

import javafx.fxml.Initializable
import javafx.print.PageOrientation
import javafx.scene.control.*
import javafx.scene.control.TableColumn.CellEditEvent
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.text.Text
import ldcapps.servicehelper.*
import ldcapps.servicehelper.controllers.tools.AddCar
import ldcapps.servicehelper.controllers.tools.Panes
import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.db.DataClasses.Companion.cars
import ldcapps.servicehelper.db.DataClasses.Companion.companies
import ldcapps.servicehelper.db.DataClasses.Companion.individuals
import ldcapps.servicehelper.db.DataClasses.Companion.owners
import ldcapps.servicehelper.db.DataClasses.Companion.reports
import ldcapps.servicehelper.db.DataClasses.Companion.user
import ldclibs.javafx.controls.*
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.net.URL
import java.time.format.DateTimeFormatter
import java.util.*


class OOController : Initializable {
    lateinit var createActBtn: Button
    lateinit var confirmBtn: Button
    lateinit var cancelBtn: Button
    lateinit var workNameTf: AutoCompletedTextField
    lateinit var workPriceTf: PriceTextField
    lateinit var workExecutorsCb: ComboBox<String>
    lateinit var excelWorkBtn: Button
    lateinit var dpcNameTf: AutoCompletedTextField
    lateinit var dpcCountTf: IntTextField
    lateinit var dpcPriceTf: PriceTextField
    lateinit var dpcSumTf: PriceTextField
    lateinit var dpcUnitCb: ComboBox<String>
    lateinit var dpcStateCb: ComboBox<String>
    lateinit var dpcExecutorsCb: ComboBox<String>
    lateinit var excelDPCBtn: Button
    lateinit var dfcNameTf: AutoCompletedTextField
    lateinit var dfcCountTf: IntTextField
    lateinit var dfcUnitCb: ComboBox<String>
    lateinit var dfcStateCb: ComboBox<String>
    lateinit var dfcExecutorsCb: ComboBox<String>
    lateinit var excelDFCBtn: Button
    lateinit var totalWorkPriceTf: PriceTextField
    lateinit var totalDPCPriceTf: PriceTextField
    lateinit var totalPriceTf: PriceTextField
    lateinit var carNumberTf: AutoCompletedTextField
    lateinit var carMileageTf: IntTextField
    lateinit var ooNumberTf: IntTextField
    lateinit var registrationDp: DatePicker
    lateinit var executionDp: DatePicker
    lateinit var ownerTf: MyTextField
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

    private var oo = OOAndBill()

    private var action = 0
    private var path = ""

    private var cash = false

    private var billPos = mutableListOf<Int>()

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        workNameTf.items = data.works + data.worksPrices.map { it.key }
        dpcNameTf.items = data.dpcs + data.dpcsPrices.map { it.key }
        dfcNameTf.items = data.dfcs

        workNameTf.onAutoCompleted = {
            workPriceTf.text = data.worksPrices[it]?.get(oo.carModel)?.toString() ?: "0.0"
            workPriceTf.requestFocus()
        }

        excelWorkBtn.setOnAction { _ ->
            Dialogs.getFile(MainController.stage, null, "xlsx" to "Excel")?.let { path ->
                XSSFWorkbook(File(path)).use {
                    val sheet = it.getSheetAt(0)
                    val sequence = Dialogs.filter(0, "Позиция", "Цена", "Исполнитель")
                    var row: Row? = sheet.getRow(sequence.first)

                    while (row != null) {
                        oo.works.add(
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
                        oo.dpcs.add(
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
                        oo.dfcs.add(
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

        dpcNameTf.onAutoCompleted = {
            dpcPriceTf.text = data.dpcsPrices[it]?.get(oo.carModel)?.second?.toString() ?: "0.0"
            dpcUnitCb.value = data.dpcsPrices[it]?.get(oo.carModel)?.first ?: "ш"
            dpcPriceTf.requestFocus()
        }

        carNumberTf.items = cars.map { it.keyNum }

        createActBtn.setOnAction {
            Dialogs.confirmation("При создании акта ЗН сохранится автоматически") {
                confirmBtn.fire()
                Windows.act()?.initOO(oo, path)
            }
        }
        cancelBtn.setOnAction {
            when (action) {
                0 -> Dialogs.confirmation("Подтвердите выход") {
                    MainController.closeSelectedTab()
                }
                4 -> {
                    registrationDp.editor.text = registrationDateTx.text
                    executionDp.editor.text = executionDate1Tx.text
                    carMileageTf.text = carMileageLb.text
                }
            }
            confirmBtn.text = "Подтвердить"
            action = 0
        }
        confirmBtn.setOnAction {
            when (action) {
                0 -> {
                    refresh()
                    workTable.selectionModel.clearSelection()
                    dpcTable.selectionModel.clearSelection()
                    dfcTable.selectionModel.clearSelection()
                    billTable.selectionModel.clearSelection()
                    if (path == "")
                        path =
                            "${settings.oosLocate}\\${oo.customerPA?.let { "Безнал" } ?: "Нал"}\\Заказ-Наряд №${oo.number} от ${oo.registrationDate} от ${oo.customer}." +
                                    if (cash) "oo" else "oab"
                    path = Regex("[/*?\"<>|]").replace(path, "")
                    toJSON(path, oo)

                    reports.removeIf { it.number == oo.number && it.carNumber == oo.carNumber }
                    reports.add(
                        DataClasses.Report(
                            user.name,
                            oo.number,
                            if (oo.customerPA == null) 0 else 1,
                            oo.abbreviatedCompanyName,
                            oo.customer,
                            oo.customerOwner,
                            oo.carNumber,
                            oo.carMileage.toIntOrNull(),
                            oo.registrationDate,
                            oo.executionDate,
                            oo.hourNorm,
                            roundPrice(oo.works.sumOf { it.price }),
                            roundPrice(oo.dpcs.sumOf { it.price * it.count }),
                            oo.vat,
                            oo.works.size,
                            oo.dpcs.size,
                            oo.dfcs.size
                        )
                    )

                    oo.works.forEach {
                        if (data.worksPrices[it.name] == null)
                            data.worksPrices[it.name] = mutableMapOf()
                        if (data.worksPrices[it.name]!![oo.carModel] == null)
                            data.worksPrices[it.name]!![oo.carModel] = it.price
                    }
                    oo.dpcs.forEach {
                        if (data.dpcsPrices[it.name] == null)
                            data.dpcsPrices[it.name] = mutableMapOf()
                        if (data.dpcsPrices[it.name]!![oo.carModel] == null)
                            data.dpcsPrices[it.name]!![oo.carModel] = it.unit to it.price
                    }
                    oo.dfcs.forEach { if (!data.dfcs.contains(it.name)) data.dfcs.add(it.name) }

                    toJSON(".data", data)

                    Dialogs.confirmation("Заказ-Наряд №${oo.number} успешно создан и находится по пути:\n$path\nРаспечатать его?") {
                        oo.customerPA?.let { Dialogs.print(MainController.stage, PageOrientation.PORTRAIT, ooAp, billAp) }
                            ?: Dialogs.print(MainController.stage, PageOrientation.PORTRAIT, ooAp)
                    }
                }
                1 -> {
                    if (isNotNull(workNameTf, workPriceTf, workPriceTf)) {
                        oo.works.add(Work(workNameTf.text, workPriceTf.text.toDouble(), workExecutorsCb.value))
                        refresh()
                        workNameTf.text = ""
                        workPriceTf.text = ""
                        workExecutorsCb.value = user.standardWorker
                    }
                }
                2 -> {
                    if (isNotNull(dpcNameTf, dpcCountTf, dpcUnitCb, dpcStateCb) && (isNotNull(
                            dpcPriceTf
                        ) || isNotNull(dpcSumTf))
                    ) {
                        if (isNotNull(dpcCountTf, if (isNotNull(dpcSumTf)) dpcSumTf else dpcPriceTf)) {
                            oo.dpcs.add(
                                DPC(
                                    dpcNameTf.text,
                                    dpcUnitCb.value,
                                    dpcCountTf.text.toInt(),
                                    dpcStateCb.value,
                                    if (isNotNull(dpcSumTf)) dpcSumTf.text.toDouble() / dpcCountTf.text.toInt() else dpcPriceTf.text.toDouble(),
                                    dpcExecutorsCb.value
                                )
                            )
                            refresh()
                            dpcNameTf.text = ""
                            dpcCountTf.text = ""
                            dpcPriceTf.text = ""
                            dpcSumTf.text = ""
                            dpcUnitCb.value = user.standardUnit
                            dpcStateCb.value = user.standardState
                            dpcExecutorsCb.value = user.standardWorker
                        }
                    }
                }
                3 -> {
                    if (isNotNull(dfcNameTf, dfcCountTf, dfcUnitCb, dfcStateCb)) {
                        oo.dfcs.add(
                            DFC(
                                dfcNameTf.text, dfcUnitCb.value,
                                dfcCountTf.text.toInt(), dfcStateCb.value, dfcExecutorsCb.value
                            )
                        )
                        refresh()
                        dfcNameTf.text = ""
                        dfcCountTf.text = ""
                        dfcUnitCb.value = user.standardUnit
                        dfcStateCb.value = user.standardState
                        dfcExecutorsCb.value = user.standardWorker
                    }
                }
                4 -> {
                    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

                    oo.executionDate = DataClasses.Date(executionDp.value)
                    oo.registrationDate = DataClasses.Date(registrationDp.value)
                    oo.carMileage = carMileageTf.text
                    oo.number = ooNumberTf.text.toInt()
                    registrationDateTx.text = formatter.format(registrationDp.value)
                    executionDate1Tx.text = formatter.format(executionDp.value)
                    executionDate2Tx.text = formatter.format(executionDp.value)
                    executionDate3Tx.text = formatter.format(executionDp.value)
                    executionDate4Tx.text = formatter.format(executionDp.value)
                    executionDate5Tx.text = formatter.format(executionDp.value)
                    carMileageLb.text = carMileageTf.text
                    ooNumberTx.text = "ЗАКАЗ-НАРЯД №${oo.number}"
                    billNumberTx.text = "СЧЕТ №${oo.number} от ${formatter.format(executionDp.value)}"
                    MainController.selectedTab.text = "Б ЗН №${oo.number}, ${oo.carModel}"
                }
                5 -> {
                    cars.find { it.keyNum == carNumberTf.text }?.let {
                        oo.carNumber = it.number
                        oo.carModel = it.model
                        oo.carMileage = carMileageTf.text
                        oo.carEngine = it.engine
                        oo.carYear = it.year
                        oo.carVIN = it.vin
                        oo.customerOwner = it.owner
                        oo.customerPA = ""
                        oo.customerBIK = ""
                        oo.customerPRN = ""
                        oo.customerContractDate = ""
                        oo.customer = owners.find { o -> o.owner == it.owner }?.company ?: it.owner
                        individuals.find { i -> i.individual == it.owner }?.let { i ->
                            oo.customerAddress = i.address
                        } ?: run {
                            companies.find { c -> c.company == oo.customer }?.let { c ->
                                oo.customerAddress = c.address
                                oo.customerPA = c.pa
                                oo.customerBIK = c.bik
                                oo.customerPRN = c.prn
                                oo.customerContractDate = c.contractDate
                            }
                        }
                        initCar()
                    }
                        ?: Dialogs.confirmation("Данного гос. номера авто нет в БД, но вы можите добавить его в меню \"Инструменты\"") {
                            Panes.ADD_CAR.show<AddCar>(Windows.tools()!!.addCarTb).keyTf.text = carNumberTf.text
                        }
                }
            }
            confirmBtn.text = "Подтвердить"
            action = 0
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

        workNumberCol.setValueFactory { oo.works.indexOf(it) + 1 }
        workNameCol.setValueFactory(Work::name.name)
        workHourNormCol.setPriceValueFactory { it.price / oo.hourNorm }
        workPriceCol.setPriceValueFactory { it.price }
        workExecutorsCol.setValueFactory(Work::executor.name)
        dpcNumberCol.setValueFactory { oo.dpcs.indexOf(it) + 1 }
        dpcDetailCol.setValueFactory(DPC::name.name)
        dpcUnitCol.setValueFactory { if (it.unit == "ш") "шт." else "л." }
        dpcCountCol.setValueFactory { it.count }
        dpcStateCol.setValueFactory { if (it.state == "н") "новая" else "б/у" }
        dpcPriceCol.setPriceValueFactory { it.price }
        dpcSumCol.setPriceValueFactory { it.count * it.price }
        dpcExecutorsCol.setValueFactory(DPC::executor.name)
        dfcNumberCol.setValueFactory { oo.dfcs.indexOf(it) + 1 }
        dfcDetailCol.setValueFactory(DFC::name.name)
        dfcUnitCol.setValueFactory { if (it.unit == "ш") "шт." else "л." }
        dfcCountCol.setValueFactory { it.count }
        dfcStateCol.setValueFactory { if (it.state == "н") "новая" else "б/у" }
        dfcExecutorsCol.setValueFactory(DFC::executor.name)
        billNumberCol.setValueFactory { it + 1 }
        billNameCol.setValueFactory {
            if (it < oo.works.size) oo.works[it].name else oo.dpcs[it - oo.works.size].name
        }
        billUnitCol.setValueFactory {
            if (it < oo.works.size) "н.ч" else if (oo.dpcs[it - oo.works.size].unit == "ш") "шт." else "л."
        }
        billCountCol.setPriceValueFactory {
            if (it < oo.works.size) oo.works[it].price / oo.hourNorm
            else oo.dpcs[it - oo.works.size].count.toDouble()
        }
        billPriceCol.setPriceValueFactory {
            if (it < oo.works.size) oo.hourNorm
            else oo.dpcs[it - oo.works.size].price
        }
        billSumCol.setPriceValueFactory {
            if (it < oo.works.size) oo.works[it].price
            else (oo.dpcs[it - oo.works.size].price * oo.dpcs[it - oo.works.size].count)
        }
        billVatCol.setValueFactory { oo.vat?.toString() ?: "Без налога" }
        billVatPriceCol.setPriceValueFactory {
            when {
                oo.vat == null -> null
                it < oo.works.size -> roundPrice(oo.vat!!.div(100) * oo.works[it].price)
                else -> oo.dpcs[it - oo.works.size].price * (oo.vat!!.div(100))
            }
        }
        billTotalPriceWithVatCol.setPriceValueFactory {
            if (it < oo.works.size) oo.works[it].price * (oo.vat?.div(100)?.plus(1) ?: 1.0)
            else oo.dpcs[it - oo.works.size].price * (oo.vat?.div(100)?.plus(1) ?: 1.0)
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

    fun fill(oo: OOAndBill, path: String = "") {
        this.path = path
        this.oo = oo
        ooNumberTf.text = oo.number.toString()
        refresh()

        registrationDp.value = oo.registrationDate.toLocalDate()
        executionDp.value = oo.executionDate.toLocalDate()
        registrationDateTx.text = oo.registrationDate.get()
        executionDate1Tx.text = oo.executionDate.get()
        executionDate2Tx.text = oo.executionDate.get()
        executionDate3Tx.text = oo.executionDate.get()
        executionDate4Tx.text = oo.executionDate.get()
        executionDate5Tx.text = oo.executionDate.get()
        carMileageLb.text = oo.carMileage
        initCar()
    }

    private fun initCar() {
        carNumberTf.text = oo.carNumber
        carMileageTf.text = oo.carMileage
        carModelLb.text = oo.carModel
        carEngineLb.text = oo.carEngine
        carYearLb.text = oo.carYear
        carVINLb.text = oo.carVIN
        carNumberLb.text = oo.carNumber
        ownerTf.text = oo.customerOwner
        ownerTx.text = "Владелец: ${oo.customerOwner}"
        customerAddress.text = "Адрес: ${oo.customerAddress}"
        oo.customerPA?.let {
            MainController.selectedTab.text = "Б ЗН №${oo.number}, ${oo.carModel}"
            cashTx.text = "Безналичный"
            cash = false
            customerPA1Tx.text = "Р/с: $it в ${oo.customerBank} БИК ${oo.customerBIK}"
            customerPA2Tx.text =
                "Плательщик: ${oo.customer}, Адрес: ${oo.customerAddress}, Р/с: ${oo.customerPA} в ${oo.customerBank}"
            customerPRNTx.text = "УНП - ${oo.customerPRN}"
            ooToBillTx.text =
                "Является актом выполненных работ к счету №${oo.number} от ${oo.executionDate.get()}"
            executor7Tx.text = oo.companyName
            executor8Tx.text = oo.abbreviatedCompanyName
            executorPA2Tx.text =
                "Р/с - ${oo.companyPA} в ${oo.companyBank}, ${oo.companyAddress}, Адрес банка - ${oo.companyBankAddress}, " +
                        "БИК - ${oo.companyBIK}, УНП - ${oo.companyPRN}, " +
                        "Почт. адрес - ${oo.companyAddress}, Тел. - +${oo.companyPhone}"
            contractDateTx.text = "договор б/н от ${oo.customerContractDate}"
            billNumberTx.text = "Счет №${oo.number} от ${oo.registrationDate.get()}"
            customer5Tx.text = "Заказчик: ${oo.customer}"
            customer6Tx.text = oo.customer
            billAp.isVisible = true
        } ?: run {
            MainController.selectedTab.text = "Н ЗН №${oo.number}, ${oo.carModel}"
            cashTx.text = "Наличный"
            customerPA1Tx.text = ""
            customerPRNTx.text = ""
            cash = true
            customerPA2Tx.text = "Плательщик: ${oo.customer}"
            billAp.isVisible = false
        }
        executor1Tx.text = oo.companyName
        executor2Tx.text = "Исполнитель: ${oo.companyName}"
        executor3Tx.text = oo.abbreviatedCompanyName
        executor4Tx.text = oo.abbreviatedCompanyName
        executor5Tx.text = oo.abbreviatedCompanyName
        executor6Tx.text = oo.companyName
        executorPA1Tx.text =
            "УНП - ${oo.companyPRN}, ${oo.companyAddress}, Р/с - ${oo.companyPA} в ${oo.companyBank}, " +
                    "БИК - ${oo.companyBIK}, Адрес банка - ${oo.companyBankAddress}"
        customer1Tx.text = oo.customer
        customer2Tx.text = oo.customer
        customer3Tx.text = oo.customer
        customer4Tx.text = oo.customer
        ooNumberTx.text = "ЗАКАЗ-НАРЯД №${oo.number}"
    }

    private fun refresh() {
        workTable.items = oo.works.toFXList()
        workTable.refresh()
        dpcTable.items = oo.dpcs.toFXList()
        dpcTable.refresh()
        dfcTable.items = oo.dfcs.toFXList()
        dfcTable.refresh()
        billPos = mutableListOf()

        for (i in 0 until oo.works.size + oo.dpcs.size) billPos.add(i)

        billTable.items = billPos.toFXList()
        billTable.refresh()
        dpcTable.layoutY = 315.0 + oo.works.size * 20
        dfcTable.layoutY = 380.0 + (oo.works.size + oo.dpcs.size) * 20
        ooFooterAp.layoutY = 445.0 + (oo.works.size + oo.dpcs.size + oo.dfcs.size) * 20
        billFooterAp.layoutY = 250.0 + (billPos.size * 20)
        workTable.prefHeight = 57.0 + oo.works.size * 20
        dpcTable.prefHeight = 57.0 + oo.dpcs.size * 20
        dfcTable.prefHeight = 57.0 + oo.dfcs.size * 20
        billTable.prefHeight = 37.0 + billPos.size * 20

        val workPrice = oo.works.sumOf { it.price }
        val dpcPrice = oo.dpcs.sumOf { it.price * it.count }

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
        vatTx.text = oo.vat?.toString() ?: "Без налога"
        oo.vat?.let {
            vatPriceTx.text = roundPrice((workPrice + dpcPrice) * oo.vat!! / 100).toString()
            totalPriceWithVAT1Tx.value = (workPrice + dpcPrice) * (oo.vat!! / 100 + 1)
            endPriceTx.text = numToStr(((workPrice + dpcPrice) * (oo.vat!! / 100 + 1)))
            totalPrice3Tx.text =
                "Всего к оплате с НДС: ${numToStr((workPrice + dpcPrice) * (oo.vat!! / 100 + 1))}"
            totalPriceWithVAT2Tx.text =
                "В том числе НДС: ${numToStr((workPrice + dpcPrice) * (oo.vat!! / 100 + 1))}"
        } ?: run {
            vatPriceTx.text = "-"
            totalPriceWithVAT1Tx.value = workPrice + dpcPrice
            endPriceTx.text = numToStr(workPrice + dpcPrice)
            totalPrice3Tx.text = "Всего к оплате с НДС: ${numToStr(workPrice + dpcPrice)}"
            totalPriceWithVAT2Tx.text = "В том числе НДС: Без НДС"
        }
        var totalPos = oo.works.size + oo.dpcs.size + oo.dfcs.size - 19

        if (totalPos > 1) {
            ooAp.prefHeight = 1130.0 + totalPos * 20
            ooAndBillAp.prefHeight = 1130.0 + totalPos * 20
        }

        totalPos = oo.works.size + oo.dpcs.size - 34

        if (oo.works.size + oo.dpcs.size > 35)
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
                oo.works.remove(workTable.selectionModel.selectedItem)
                refresh()
            } ?: Dialogs.warning("Выберите позицию для удаления")
        }
    }

    fun onDPCTableKeyPressed(event: KeyEvent) {
        if (event.code == KeyCode.DELETE && event.isControlDown) {
            dpcTable.selectionModel.selectedItem?.let {
                oo.dpcs.remove(dpcTable.selectionModel.selectedItem)
                refresh()
            } ?: Dialogs.warning("Выберите позицию для удаления")
        }
    }

    fun onDFCTableKeyPressed(event: KeyEvent) {
        if (event.code == KeyCode.DELETE && event.isControlDown) {
            dfcTable.selectionModel.selectedItem?.let {
                oo.dfcs.remove(dfcTable.selectionModel.selectedItem)
                refresh()
            } ?: Dialogs.warning("Выберите позицию для удаления")
        }
    }

    fun changeWorkNameCellEvent(editEvent: CellEditEvent<Work, String>) {
        workTable.selectionModel.selectedItem.name = editEvent.newValue!!
        refresh()
    }

    fun changeWorkPriceCellEvent(editEvent: CellEditEvent<Work, String>) {
        totalWorkPriceTf.text = oo.works.sumOf { it.price }.toString()
        totalPriceTf.text = (oo.works.sumOf { it.price } + oo.dpcs.sumOf { it.price * it.count }).toString()
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
        totalDPCPriceTf.text = oo.dpcs.sumOf { it.price }.toString()
        totalPriceTf.text = (oo.works.sumOf { it.price } + oo.dpcs.sumOf { it.price * it.count }).toString()
        dpcTable.refresh()
        refresh()
    }

    fun changeDPCPriceCellEvent(editEvent: CellEditEvent<DPC, String?>) {
        dpcTable.selectionModel.selectedItem.price = roundPrice(editEvent.newValue!!.toDouble())
        totalDPCPriceTf.text = oo.dpcs.sumOf { it.price * it.count }.toString()
        totalPriceTf.text = (oo.works.sumOf { it.price } + oo.dpcs.sumOf { it.price * it.count }).toString()
        dpcTable.refresh()
        refresh()
    }

    fun changeDPCSumCellEvent(editEvent: CellEditEvent<DPC, String?>) {
        totalDPCPriceTf.text = oo.dpcs.sumOf { it.price * it.count }.toString()
        totalPriceTf.text = (oo.works.sumOf { it.price } + oo.dpcs.sumOf { it.price * it.count }).toString()
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
        var carNumber: String = "",
        var carModel: String = "",
        var carMileage: String = "",
        var carEngine: String = "",
        var carYear: String = "",
        var carVIN: String = "",
        var customerOwner: String = "",
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
        var state: String = "", var price: Double = 0.0, var executor: String = ""
    )

    class DFC(
        var name: String = "", var unit: String = "", var count: Int = 0,
        var state: String = "", var executor: String = ""
    )

    private fun roundPrice(price: Double) = String.format("%.2f", price).replace(",", ".").toDouble()

    private fun numToStr(n: Double): String {
        val forms = arrayOf(
            arrayOf("рубль", "рубля", "рублей"),
            arrayOf("тысяча", "тысячи", "тысяч"),
            arrayOf("миллион", "миллиона", "миллионов"),
            arrayOf("миллиард", "миллиарда", "миллиардов"),
            arrayOf("триллион", "триллиона", "триллионов")
        )
        var kops: String
        var rub: Int
        String.format("%.2f", n).replace(",", ".").split(".").let {
            rub = it[0].toInt()
            kops = it[1]
        }
        val kop = kops.toInt()
        if (rub == 0)
            return "Ноль рублей $kops ${
                when (kop % 10) {
                    1 -> "копейка"
                    2, 3, 4 -> "копейки"
                    else -> "копеек"
                }
            }"
        val segments = ArrayList<Int>()
        while (rub > 999) {
            segments.add(0, rub % 1000)
            rub /= 1000
        }
        segments.add(0, rub)
        var str = ""
        var sSize = segments.size - 1
        segments.forEachIndexed { i, it ->
            if (it != 0 || i == 0 || i == segments.size - 1) {
                var r1 = 0
                var r2 = 0
                var r3 = it
                var r4 = it
                when (it.toString().length) {
                    2 -> {
                        r2 = it.toString().take(1).toInt()
                        r3 = it.toString().drop(1).toInt()
                    }
                    3 -> {
                        r1 = it.toString().take(1).toInt()
                        r2 = it.toString().substring(1, 2).toInt()
                        r3 = it.toString().drop(2).toInt()
                        r4 = it.toString().drop(1).toInt()
                    }
                }
                if (it > 99) str += arrayOf(
                    "", "сто ", "двести ", "триста ", "четыреста ",
                    "пятьсот ", "шестьсот ", "семьсот ", "восемьсот ", "девятьсот "
                )[r1]
                str += when {
                    r4 > 20 -> arrayOf(
                        "", "десять ", "двадцать ", "тридцать ", "сорок ", "пятьдесят ",
                        "шестьдесят ", "семьдесят ", "восемьдесят ", "девяносто "
                    )[r2] + if (sSize == 1)
                        arrayOf(
                            "", "одна ", "две ", "три ", "четыре ", "пять ", "шесть ", "семь ", "восемь ", "девять "
                        )[r3]
                    else
                        arrayOf(
                            "", "один ", "два ", "три ", "четыре ", "пять ", "шесть ", "семь ", "восемь ", "девять "
                        )[r3]
                    r4 > 9 -> arrayOf(
                        "", "десять ", "одиннадцать ", "двенадцать ", "тринадцать ", "четырнадцать ", "пятнадцать ",
                        "шестнадцать ", "семнадцать ", "восемнадцать ", "девятнадцать ", "двадцать "
                    )[r4 - 9]
                    else -> {
                        if (sSize == 1)
                            arrayOf(
                                "", "одна ", "две ", "три ", "четыре ", "пять ", "шесть ", "семь ", "восемь ", "девять "
                            )[r3]
                        else
                            arrayOf(
                                "", "один ", "два ", "три ", "четыре ", "пять ", "шесть ", "семь ", "восемь ", "девять "
                            )[r3]
                    }
                }
                str += "${
                    when (it % 10) {
                        1 -> forms[sSize][0]
                        2, 3, 4 -> forms[sSize][1]
                        else -> forms[sSize][2]
                    }
                } "
            }
            sSize--
        }
        str += "$kops ${
            when (kop % 10) {
                1 -> "копейка"
                2, 3, 4 -> "копейки"
                else -> "копеек"
            }
        }"
        return str[0].toUpperCase() + str.drop(1)
    }
}