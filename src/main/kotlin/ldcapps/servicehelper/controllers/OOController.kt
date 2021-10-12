package ldcapps.servicehelper.controllers

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.print.PageOrientation
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import ldcapps.servicehelper.*
import ldcapps.servicehelper.controllers.tools.AddCarController
import ldcapps.servicehelper.controllers.tools.Tools
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
import java.text.DecimalFormat
import java.util.*

class OOController : Initializable {
    @FXML
    private lateinit var mainVBox: VBox

    @FXML
    private lateinit var firstHBox: HBox

    @FXML
    private lateinit var positionsVBox: VBox

    @FXML
    private lateinit var createActBtn: Button

    @FXML
    private lateinit var confirmBtn: Button

    @FXML
    private lateinit var cancelBtn: Button

    @FXML
    private lateinit var workNameTf: AutoCompletedTextField<Data.Hint>

    @FXML
    private lateinit var workPriceTf: PriceTextField

    @FXML
    private lateinit var workExecutorsCb: ComboBox<String>

    @FXML
    private lateinit var excelWorkBtn: Button

    @FXML
    private lateinit var dpcNameTf: AutoCompletedTextField<Data.Hint>

    @FXML
    private lateinit var dpcCountTf: IntTextField

    @FXML
    private lateinit var dpcPriceTf: PriceTextField

    @FXML
    private lateinit var dpcSumTf: PriceTextField

    @FXML
    private lateinit var dpcUnitCb: ComboBox<String>

    @FXML
    private lateinit var dpcStateCb: ComboBox<String>

    @FXML
    private lateinit var dpcExecutorsCb: ComboBox<String>

    @FXML
    private lateinit var excelDPCBtn: Button

    @FXML
    private lateinit var dfcNameTf: AutoCompletedTextField<Data.Hint>

    @FXML
    private lateinit var dfcCountTf: IntTextField

    @FXML
    private lateinit var dfcUnitCb: ComboBox<String>

    @FXML
    private lateinit var dfcStateCb: ComboBox<String>

    @FXML
    private lateinit var dfcExecutorsCb: ComboBox<String>

    @FXML
    private lateinit var excelDFCBtn: Button

    @FXML
    private lateinit var totalWorkPriceTf: PriceTextField

    @FXML
    private lateinit var totalDPCPriceTf: PriceTextField

    @FXML
    private lateinit var totalPriceTf: PriceTextField

    @FXML
    private lateinit var carNumberTf: AutoCompletedTextField<String>

    @FXML
    private lateinit var carMileageTf: IntTextField

    @FXML
    private lateinit var ooNumberTf: IntTextField

    @FXML
    private lateinit var registrationDp: DatePicker

    @FXML
    private lateinit var executionDp: DatePicker

    @FXML
    private lateinit var ownerTf: MyTextField

    @FXML
    private lateinit var previewBtn: Button

    @FXML
    private lateinit var ooAndBillScroll: ScrollPane

    @FXML
    private lateinit var ooAndBillAp: AnchorPane

    @FXML
    private lateinit var ooAp: AnchorPane

    @FXML
    private lateinit var ooNumberTx: Label

    @FXML
    private lateinit var ooToBillTx: Label

    @FXML
    private lateinit var executor1Tx: Text

    @FXML
    private lateinit var executorPA1Tx: Label

    @FXML
    private lateinit var customer1Tx: Text

    @FXML
    private lateinit var customerAddress: Text

    @FXML
    private lateinit var customerPA1Tx: Text

    @FXML
    private lateinit var customerPRNTx: Text

    @FXML
    private lateinit var ownerTx: Text

    @FXML
    private lateinit var executor2Tx: Text

    @FXML
    private lateinit var registrationDateTx: Label

    @FXML
    private lateinit var executionDate1Tx: Label

    @FXML
    private lateinit var executionDate2Tx: Label

    @FXML
    private lateinit var cashTx: Label

    @FXML
    private lateinit var carModelLb: Label

    @FXML
    private lateinit var carMileageLb: Label

    @FXML
    private lateinit var carEngineLb: Label

    @FXML
    private lateinit var carYearLb: Label

    @FXML
    private lateinit var carVINLb: Label

    @FXML
    private lateinit var carNumberLb: Label

    @FXML
    private lateinit var workTable: TableView<Work>

    @FXML
    private lateinit var workNumberCol: Column<Work>

    @FXML
    private lateinit var workNameCol: Column<Work>

    @FXML
    private lateinit var workHourNormCol: Column<Work>

    @FXML
    private lateinit var workPriceCol: Column<Work>

    @FXML
    private lateinit var workExecutorsCol: Column<Work>

    @FXML
    private lateinit var dpcTable: TableView<DPC>

    @FXML
    private lateinit var dpcNumberCol: Column<DPC>

    @FXML
    private lateinit var dpcDetailCol: Column<DPC>

    @FXML
    private lateinit var dpcUnitCol: Column<DPC>

    @FXML
    private lateinit var dpcCountCol: Column<DPC>

    @FXML
    private lateinit var dpcStateCol: Column<DPC>

    @FXML
    private lateinit var dpcPriceCol: Column<DPC>

    @FXML
    private lateinit var dpcSumCol: Column<DPC>

    @FXML
    private lateinit var dpcExecutorsCol: Column<DPC>

    @FXML
    private lateinit var dfcTable: TableView<DFC>

    @FXML
    private lateinit var dfcNumberCol: Column<DFC>

    @FXML
    private lateinit var dfcDetailCol: Column<DFC>

    @FXML
    private lateinit var dfcUnitCol: Column<DFC>

    @FXML
    private lateinit var dfcCountCol: Column<DFC>

    @FXML
    private lateinit var dfcStateCol: Column<DFC>

    @FXML
    private lateinit var dfcExecutorsCol: Column<DFC>

    @FXML
    private lateinit var ooFooterAp: AnchorPane

    @FXML
    private lateinit var workPrice1Tx: PriceLabel

    @FXML
    private lateinit var detailPrice1Tx: PriceLabel

    @FXML
    private lateinit var totalPrice1Tx: PriceLabel

    @FXML
    private lateinit var startPriceTx: Label

    @FXML
    private lateinit var workPrice2Tx: PriceLabel

    @FXML
    private lateinit var detailPrice2Tx: PriceLabel

    @FXML
    private lateinit var totalPrice2Tx: PriceLabel

    @FXML
    private lateinit var vatTx: Label

    @FXML
    private lateinit var vatPriceTx: Label

    @FXML
    private lateinit var totalPriceWithVAT1Tx: PriceLabel

    @FXML
    private lateinit var endPriceTx: Label

    @FXML
    private lateinit var executionDate3Tx: Text

    @FXML
    private lateinit var customer2Tx: Label

    @FXML
    private lateinit var executor3Tx: Text

    @FXML
    private lateinit var executionDate4Tx: Text

    @FXML
    private lateinit var executor4Tx: Text

    @FXML
    private lateinit var executionDate5Tx: Text

    @FXML
    private lateinit var customer4Tx: Label

    @FXML
    private lateinit var customer3Tx: Label

    @FXML
    private lateinit var executor5Tx: Label

    @FXML
    private lateinit var billAp: AnchorPane

    @FXML
    private lateinit var executor6Tx: Text

    @FXML
    private lateinit var executorPA2Tx: Label

    @FXML
    private lateinit var billNumberTx: Label

    @FXML
    private lateinit var contractDateTx: Label

    @FXML
    private lateinit var customer5Tx: Text

    @FXML
    private lateinit var customerPA2Tx: Label

    @FXML
    private lateinit var billTable: TableView<Int>

    @FXML
    private lateinit var billNumberCol: Column<Int>

    @FXML
    private lateinit var billNameCol: Column<Int>

    @FXML
    private lateinit var billUnitCol: Column<Int>

    @FXML
    private lateinit var billCountCol: Column<Int>

    @FXML
    private lateinit var billPriceCol: Column<Int>

    @FXML
    private lateinit var billSumCol: Column<Int>

    @FXML
    private lateinit var billVatCol: Column<Int>

    @FXML
    private lateinit var billVatPriceCol: Column<Int>

    @FXML
    private lateinit var billTotalPriceWithVatCol: Column<Int>

    @FXML
    private lateinit var billFooterAp: AnchorPane

    @FXML
    private lateinit var totalPrice3Tx: Text

    @FXML
    private lateinit var totalPriceWithVAT2Tx: Text

    @FXML
    private lateinit var executor7Tx: Text

    @FXML
    private lateinit var customer6Tx: Text

    @FXML
    private lateinit var executor8Tx: Text

    private var ooAndBill = OOAndBill()

    private var action = ButtonActions.CONFIRM
    private var path = ""

    private var cash = false

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
                        var row: Row? = sheet.getRow(sequence.topMargin)

                        while (row != null) {
                            ooAndBill.works.add(
                                Work(
                                    getCellValue(row, sequence.rightMargin + sequence.result[0], ""),
                                    getCellValue(row, sequence.rightMargin + sequence.result[1], 0.0),
                                    getCellValue(row, sequence.rightMargin + sequence.result[0], user.standardWorker)
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
                    var row: Row? = sheet.getRow(sequence.topMargin)

                    while (row != null) {
                        ooAndBill.dpcs.add(
                            DPC(
                                getCellValue(row!!, sequence.rightMargin + sequence.result[0], ""),
                                getCellValue(row!!, sequence.rightMargin + sequence.result[1], user.standardUnit),
                                getCellValue(row!!, sequence.rightMargin + sequence.result[2], 1),
                                getCellValue(row!!, sequence.rightMargin + sequence.result[3], user.standardState),
                                getCellValue(row!!, sequence.rightMargin + sequence.result[4], 0.0),
                                getCellValue(row!!, sequence.rightMargin + sequence.result[5], user.standardWorker),
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
                    var row: Row? = sheet.getRow(sequence.topMargin)

                    while (row != null) {
                        ooAndBill.dfcs.add(
                            DFC(
                                getCellValue(row!!, sequence.rightMargin + sequence.result[0], ""),
                                getCellValue(row!!, sequence.rightMargin + sequence.result[1], user.standardUnit),
                                getCellValue(row!!, sequence.rightMargin + sequence.result[2], 1),
                                getCellValue(row!!, sequence.rightMargin + sequence.result[3], user.standardState),
                                getCellValue(row!!, sequence.rightMargin + sequence.result[4], user.standardWorker),
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
                action = ButtonActions.CONFIRM
                confirmBtn.fire()
                Windows.act()?.initOO(ooAndBill, path)
            }
        }

        cancelBtn.setOnAction {
            when (action) {
                ButtonActions.CONFIRM -> Dialogs.confirmation("Подтвердите выход") { MainController.closeSelectedTab() }
                ButtonActions.APPLY_CHANGES -> {
                    registrationDp.value = ooAndBill.registrationDate.localDate
                    executionDp.value = ooAndBill.executionDate.localDate
                    carMileageTf.text = carMileageLb.text
                }
                else -> {}
            }
            confirmBtn.text = "Подтвердить"
            action = ButtonActions.CONFIRM
        }

        confirmBtn.setOnAction {
            when (action) {
                ButtonActions.CONFIRM -> {
                    if (ooAndBill.car == null) return@setOnAction
                    workTable.selectionModel.clearSelection()
                    dpcTable.selectionModel.clearSelection()
                    dfcTable.selectionModel.clearSelection()
                    billTable.selectionModel.clearSelection()

                    if (path == "")
                        path = "${settings.oosLocate}\\${if (cash) "Нал" else "Безнал"}\\Заказ-Наряд №${ooAndBill.number} от " +
                                "${ooAndBill.registrationDate} от ${ooAndBill.customer?.company}.${if (cash) "oo" else "oab"}"

                    path = Regex("[/*?\"<>|]").replace(path, "")

                    toJSON(path, ooAndBill)

                    val report = DataClasses.Report(
                        user.name,
                        ooAndBill.number,
                        if (cash) 0 else 1,
                        ooAndBill.executor!!.abbreviatedExecutor,
                        ooAndBill.customer!!.company,
                        ooAndBill.car!!.owner,
                        ooAndBill.car!!.number,
                        ooAndBill.carMileage,
                        ooAndBill.registrationDate,
                        ooAndBill.executionDate,
                        ooAndBill.executor!!.hourNorm,
                        roundPrice(ooAndBill.works.sumOf { it.price }).toDouble(),
                        roundPrice(ooAndBill.dpcs.sumOf { it.price * it.count }).toDouble(),
                        ooAndBill.executor!!.vat,
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
                        data.dpcs.checkHint(hint)
                    }
                    ooAndBill.dfcs.forEach {
                        val hint = Data.Hint(ooAndBill.car!!.model, it.name, null, it.count, it.state, it.unit)
                        data.dfcs.checkHint(hint)
                    }

                    toJSON(".data", data)

                    Dialogs.confirmation("Заказ-Наряд №${ooAndBill.number} успешно создан и находится по пути:\n$path\nРаспечатать его?") {
                        ooAndBill.customer?.let {
                            Dialogs.print(MainController.stage, PageOrientation.PORTRAIT, ooAp, billAp)
                        } ?: Dialogs.print(MainController.stage, PageOrientation.PORTRAIT, ooAp)
                    }
                }
                ButtonActions.ADD_WORK -> {
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
                ButtonActions.ADD_DPC -> {
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
                ButtonActions.ADD_DFC -> {
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
                ButtonActions.APPLY_CHANGES -> {
                    ooAndBill.executionDate = DataClasses.Date(executionDp.value)
                    ooAndBill.registrationDate = DataClasses.Date(registrationDp.value)
                    ooAndBill.carMileage = carMileageTf.text.toInt()
                    ooAndBill.number = ooNumberTf.text.toInt()

                    updateInfo()
                }
                ButtonActions.APPLY_CAR -> {
                    cars.find { it.keyNum == carNumberTf.text }?.let { updateCar(it) }
                        ?: Dialogs.confirmation("Данного гос. номера авто нет в БД, но вы можите добавить его в меню \"Инструменты\"") {
                            Tools.ADD_CAR.show<AddCarController>(Windows.tools()!!.addCarTb, confirmBtn.scene.window).keyTf.text = carNumberTf.text
                        }
                }
            }
            confirmBtn.text = "Подтвердить"
            action = ButtonActions.CONFIRM
        }

        previewBtn.setOnAction {
            ooAndBillScroll.isVisible = !ooAndBillScroll.isVisible

            if (ooAndBillScroll.isVisible) {
                firstHBox.children.add(1, positionsVBox)
                MainController.changeStageSize(FXMLInfo.OO)
            } else {
                mainVBox.children.add(positionsVBox)
                MainController.changeStageSize(FXMLInfo.OOCollapsed)
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

        dpcPriceTf.setOnKeyReleased {
            val count = dpcCountTf.text.toIntOrNull()
            val price = dpcPriceTf.text.toDoubleOrNull()

            if (count != null && price != null)
                dpcSumTf.text = roundPrice(price * count)
        }

        dpcCountTf.setOnKeyReleased {
            val count = dpcCountTf.text.toIntOrNull()
            val price = dpcPriceTf.text.toDoubleOrNull()

            if (count != null && price != null)
                dpcSumTf.text = roundPrice(price * count)
        }

        dpcSumTf.setOnKeyReleased {
            val count = dpcCountTf.text.toIntOrNull()
            val sum = dpcSumTf.text.toDoubleOrNull()

            if (count != null && sum != null)
                dpcPriceTf.text = roundPrice(sum / count)
        }

        workNumberCol.numCol()
        workNameCol.setValueFactory(Work::name.name)
        workHourNormCol.setPriceValueFactory { it.price / ooAndBill.executor!!.hourNorm }
        workPriceCol.setPriceValueFactory(Work::price.name)
        workExecutorsCol.setValueFactory(Work::executor.name)
        dpcNumberCol.numCol()
        dpcDetailCol.setValueFactory(DPC::name.name)
        dpcUnitCol.setValueFactory(DPC::unit.name)
        dpcCountCol.setValueFactory(DPC::count.name)
        dpcStateCol.setValueFactory(DPC::state.name)
        dpcPriceCol.setPriceValueFactory(DPC::price.name)
        dpcSumCol.setPriceValueFactory { it.count * it.price }
        dpcExecutorsCol.setValueFactory(DPC::executor.name)
        dfcNumberCol.numCol()
        dfcDetailCol.setValueFactory(DFC::name.name)
        dfcUnitCol.setValueFactory(DFC::unit.name)
        dfcCountCol.setValueFactory(DFC::count.name)
        dfcStateCol.setValueFactory(DFC::state.name)
        dfcExecutorsCol.setValueFactory(DFC::executor.name)

        billNumberCol.numCol()
        billNameCol.setValueFactory {
            ooAndBill.works.getOrNull(it)?.name ?: ooAndBill.dpcs[it - ooAndBill.works.size].name
        }
        billUnitCol.setValueFactory {
            ooAndBill.dpcs.getOrNull(it - ooAndBill.works.size)?.unit ?: "н.ч"
        }
        billCountCol.setPriceValueFactory {
            ooAndBill.dpcs.getOrNull(it - ooAndBill.works.size)?.count
                ?: (ooAndBill.works[it].price / ooAndBill.executor!!.hourNorm)
        }
        billPriceCol.setPriceValueFactory {
            ooAndBill.dpcs.getOrNull(it - ooAndBill.works.size)?.price ?: ooAndBill.executor!!.hourNorm
        }
        billSumCol.setPriceValueFactory {
            ooAndBill.works.getOrNull(it)?.price
                ?: (ooAndBill.dpcs[it - ooAndBill.works.size].price * ooAndBill.dpcs[it - ooAndBill.works.size].count)
        }
        billVatCol.setValueFactory { ooAndBill.executor!!.vat?.toString() ?: "Без налога" }
        billVatPriceCol.setPriceValueFactory {
            val vat = ooAndBill.executor?.vat?.div(100) ?: return@setPriceValueFactory null

            ooAndBill.works.getOrNull(it)?.price?.times(vat) ?: (ooAndBill.dpcs[it - ooAndBill.works.size].price * vat)
        }
        billTotalPriceWithVatCol.setPriceValueFactory {
            val vat = (ooAndBill.executor!!.vat?.div(100)?.plus(1) ?: 1.0)

            ooAndBill.works.getOrNull(it)?.price?.times(vat) ?: (ooAndBill.dpcs[it - ooAndBill.works.size].price * vat)
        }

        workNameCol.setTextFieldCellFactory()
        workPriceCol.setPriceTextFieldCellFactory()
        workExecutorsCol.setComboBoxCellFactory(*user.workers.toTypedArray())
        dpcDetailCol.setTextFieldCellFactory()
        dpcCountCol.setIntTextFieldCellFactory()
        dpcPriceCol.setPriceTextFieldCellFactory()
        dpcSumCol.setPriceTextFieldCellFactory()
        dpcStateCol.setComboBoxCellFactory("н", "б")
        dpcUnitCol.setComboBoxCellFactory("ш", "л")
        dpcExecutorsCol.setComboBoxCellFactory(*user.workers.toTypedArray())
        dfcDetailCol.setTextFieldCellFactory()
        dfcCountCol.setIntTextFieldCellFactory()
        dfcStateCol.setComboBoxCellFactory("н", "б")
        dfcUnitCol.setComboBoxCellFactory("ш", "л")
        dfcExecutorsCol.setComboBoxCellFactory(*user.workers.toTypedArray())

        dpcSumCol.onEditCommitted = EventHandler {
            it.rowValue.price = roundPrice(it.newValue!!.toDouble() / it.rowValue.count).toDouble()

            refresh()
        }
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

        registrationDp.value = ooAndBill.registrationDate.localDate
        executionDp.value = ooAndBill.executionDate.localDate

        refresh()
        updateInfo()
        updateCar()
        updateExecutor()
    }

    private fun updateCar(car: DataClasses.Car = ooAndBill.car!!) {
        ooAndBill.car = car

        val company = DataClasses.owners.find { it.owner == car.owner }?.company ?: car.owner
        companies.find { it.company == company }?.let { ooAndBill.customer = it }
            ?: individuals.find { it.individual == car.owner }?.let { ooAndBill.customerAddress = it.address }

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
        customerAddress.text = "Адрес: ${ooAndBill.customer?.address ?: ooAndBill.customerAddress}"

        if (ooAndBill.customer == null) {
            cashTx.text = "Наличный"

            customerPA1Tx.text = ""
            customerPRNTx.text = ""
            contractDateTx.text = ""

            customerPA2Tx.text = "Плательщик: ${ooAndBill.customer}"

            cash = true
            billAp.isVisible = false
        } else {
            val customer = ooAndBill.customer!!

            cashTx.text = "Безналичный"

            customerPA1Tx.text = "Р/с: ${customer.pa} в ${customer.bank} БИК ${customer.bik}"
            customerPA2Tx.text =
                "Плательщик: ${customer.company}, Адрес: ${customer.address}, Р/с: ${customer.pa} в ${customer.bank}"
            customerPRNTx.text = "УНП - ${customer.prn}"
            contractDateTx.text = "договор б/н от ${customer.contractDate}"
            customer5Tx.text = "Заказчик: ${customer.company}"
            customer6Tx.text = customer.company

            cash = false
            billAp.isVisible = true
        }

        customer1Tx.text = company
        customer2Tx.text = company
        customer3Tx.text = company
        customer4Tx.text = company
    }

    private fun updateExecutor() {
        val executor = ooAndBill.executor ?: return

        executor1Tx.text = executor.executor
        executor2Tx.text = "Исполнитель: ${executor.executor}"
        executor3Tx.text = executor.abbreviatedExecutor
        executor4Tx.text = executor.abbreviatedExecutor
        executor5Tx.text = executor.abbreviatedExecutor
        executor6Tx.text = executor.executor
        executorPA1Tx.text = "УНП - ${executor.prn}, " +
                "${executor.address}, " +
                "Р/с - ${executor.pa} в ${executor.bank}, " +
                "БИК - ${executor.bik}, " +
                "Адрес банка - ${executor.bankAddress}"

        if (cash) return

        ooToBillTx.text =
            "Является актом выполненных работ к счету №${ooAndBill.number} от ${ooAndBill.executionDate.value}"
        executorPA2Tx.text =
            "Р/с - ${executor.pa} в ${executor.bank}, " +
                    "Адрес банка - ${executor.bankAddress}, " +
                    "БИК - ${executor.bik}, " +
                    "УНП - ${executor.prn}, " +
                    "Адрес - ${executor.address}, " +
                    "Тел. - +${executor.phone}"
        executor7Tx.text = executor.executor
        executor8Tx.text = executor.abbreviatedExecutor

    }

    private fun refreshOOTables() {
        workTable.items = ooAndBill.works.toFXList()
        dpcTable.items = ooAndBill.dpcs.toFXList()
        dfcTable.items = ooAndBill.dfcs.toFXList()
        workTable.refresh()
        dpcTable.refresh()
        dfcTable.refresh()

        dpcTable.layoutY = 315.0 + ooAndBill.works.size * 20
        dfcTable.layoutY = 380.0 + (ooAndBill.works.size + ooAndBill.dpcs.size) * 20
        ooFooterAp.layoutY = 445.0 + (ooAndBill.works.size + ooAndBill.dpcs.size + ooAndBill.dfcs.size) * 20
        workTable.prefHeight = 57.0 + ooAndBill.works.size * 20
        dpcTable.prefHeight = 57.0 + ooAndBill.dpcs.size * 20
        dfcTable.prefHeight = 57.0 + ooAndBill.dfcs.size * 20

        val positions = ooAndBill.works.size + ooAndBill.dpcs.size + ooAndBill.dfcs.size
        if (positions > 20) {
            ooAp.prefHeight = 1130.0 + (positions - 20) * 20
            ooAndBillAp.prefHeight = 1130.0 + (positions - 20) * 20
        }
    }

    private fun refreshBillTable() {
        billTable.items = (0 until ooAndBill.works.size + ooAndBill.dpcs.size).toFXList()
        billTable.refresh()

        billTable.prefHeight = 37.0 + billTable.items.size * 20
        billFooterAp.layoutY = 250.0 + (billTable.items.size * 20)

        val positions = ooAndBill.works.size + ooAndBill.dpcs.size
        if (positions > 35)
            billAp.prefHeight = 1130.0 + (positions - 35) * 20
    }

    private fun refreshValues() {
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

        val vat = ooAndBill.executor?.vat

        vatTx.text = vat?.toString() ?: "Без налога"

        if (vat == null) {
            vatPriceTx.text = "-"
            totalPriceWithVAT1Tx.value = workPrice + dpcPrice
            endPriceTx.text = numToStr(workPrice + dpcPrice)
            totalPrice3Tx.text = "Всего к оплате с НДС: ${numToStr(workPrice + dpcPrice)}"
            totalPriceWithVAT2Tx.text = "В том числе НДС: Без НДС"
        } else {
            vatPriceTx.text = roundPrice((workPrice + dpcPrice) * vat / 100).toString()
            totalPriceWithVAT1Tx.value = (workPrice + dpcPrice) * (vat / 100 + 1)
            endPriceTx.text = numToStr(((workPrice + dpcPrice) * (vat / 100 + 1)))
            totalPrice3Tx.text = "Всего к оплате с НДС: ${numToStr((workPrice + dpcPrice) * (vat / 100 + 1))}"
            totalPriceWithVAT2Tx.text = "В том числе НДС: ${numToStr((workPrice + dpcPrice) * (vat / 100 + 1))}"
        }
    }

    @FXML
    private fun refresh() {
        refreshOOTables()
        refreshBillTable()
        refreshValues()
    }

    fun onPositionsMouseClicked(mouseEvent: MouseEvent) {
        action = when {
            mouseEvent.source.toString().contains("work") -> ButtonActions.ADD_WORK
            mouseEvent.source.toString().contains("dpc") -> ButtonActions.ADD_DPC
            mouseEvent.source.toString().contains("dfc") -> ButtonActions.ADD_DFC
            mouseEvent.source.toString().contains("carNumberTf") -> ButtonActions.APPLY_CAR
            else -> ButtonActions.APPLY_CHANGES
        }

        confirmBtn.text = action.value
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

    class OOAndBill(
        var number: Int = 0,
        var registrationDate: DataClasses.Date = DataClasses.Date(),
        var executionDate: DataClasses.Date = DataClasses.Date(),
        var car: DataClasses.Car? = null,
        var carMileage: Int? = null,
        var customer: DataClasses.Company? = null,
        var customerAddress: String? = null,
        var executor: DataClasses.Executor? = null,
        var works: MutableList<Work> = mutableListOf(),
        var dpcs: MutableList<DPC> = mutableListOf(),
        var dfcs: MutableList<DFC> = mutableListOf(),
    )

    class Work(
        var name: String = "",
        var price: Double = 0.0,
        var executor: String = "",
    )

    class DPC(
        var name: String = "",
        var unit: String = "",
        var count: Int = 0,
        var state: String = "",
        var price: Double = 0.0,
        var executor: String = "",
    )

    class DFC(
        var name: String = "",
        var unit: String = "",
        var count: Int = 0,
        var state: String = "",
        var executor: String = "",
    )

    enum class ButtonActions(val value: String){
        CONFIRM("Подтвердить"),
        ADD_WORK("Добавить работу"),
        ADD_DPC("Добавить дет. опл. зак."),
        ADD_DFC("Добавить дет. прин. от зак."),
        APPLY_CAR("Применить авто"),
        APPLY_CHANGES("Применить изменения"),
    }

    private fun roundPrice(price: Double) = DecimalFormat("#0.00").format(price).replace(",", ".")
}