package ldcapps.servicehelper.controllers.tools

import javafx.fxml.Initializable
import javafx.print.PageOrientation
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TableView
import javafx.scene.control.ToggleButton
import javafx.stage.Stage
import kotlinx.serialization.ExperimentalSerializationApi
import ldcapps.servicehelper.Dialogs
import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.db.DataClasses.Companion.reports
import ldcapps.servicehelper.initTableSize
import ldcapps.servicehelper.toFXList
import ldclibs.javafx.controls.Column
import liklibs.db.toLocalDate
import org.apache.poi.ss.usermodel.CellCopyPolicy
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.net.URL
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*

@ExperimentalSerializationApi
class GetReportController : Initializable {
    lateinit var confirmBtn: Button
    lateinit var fillBtn: Button
    lateinit var yearCb: ComboBox<Int>
    lateinit var monthsCb: ComboBox<String>
    lateinit var cashlessTb: ToggleButton
    lateinit var cashTb: ToggleButton
    lateinit var table: TableView<DataClasses.Report>
    lateinit var ooNumberCol: Column<DataClasses.Report>
    lateinit var executorCol: Column<DataClasses.Report>
    lateinit var customerCol: Column<DataClasses.Report>
    lateinit var ownerCol: Column<DataClasses.Report>
    lateinit var carNumberCol: Column<DataClasses.Report>
    lateinit var carMileageCol: Column<DataClasses.Report>
    lateinit var registrationDateCol: Column<DataClasses.Report>
    lateinit var executionDateCol: Column<DataClasses.Report>
    lateinit var hourNormCol: Column<DataClasses.Report>
    lateinit var totalWorkPriceCol: Column<DataClasses.Report>
    lateinit var totalDPCPriceCol: Column<DataClasses.Report>
    lateinit var totalPriceCol: Column<DataClasses.Report>
    lateinit var vatCol: Column<DataClasses.Report>
    lateinit var vatPriceCol: Column<DataClasses.Report>
    lateinit var totalPriceWithVATCol: Column<DataClasses.Report>
    lateinit var workCountCol: Column<DataClasses.Report>
    lateinit var dfcCountCol: Column<DataClasses.Report>
    lateinit var dpcCountCol: Column<DataClasses.Report>
    lateinit var totalPosCountCol: Column<DataClasses.Report>

    private val months = mutableListOf(
        "Январь", "Февраль", "Март", "Апрель",
        "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
    )

    private val years = reports.map { it.exDate.year }.distinct()

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        table.initTableSize(1, 4, 4, 4, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 1, 1, 1, 1)

        ooNumberCol.setValueFactory(DataClasses.Report::number.name)
        executorCol.setValueFactory(DataClasses.Report::executor.name)
        customerCol.setValueFactory(DataClasses.Report::customer.name)
        ownerCol.setValueFactory(DataClasses.Report::owner.name)
        carNumberCol.setValueFactory(DataClasses.Report::carNumber.name)
        carMileageCol.setValueFactory { if (it.carMileage != 0) it.carMileage.toString() else "-" }
        registrationDateCol.setValueFactory(DataClasses.Report::regDate.name)
        executionDateCol.setValueFactory(DataClasses.Report::exDate.name)
        hourNormCol.setValueFactory(DataClasses.Report::hourNorm.name)
        totalWorkPriceCol.setValueFactory(DataClasses.Report::totalWorkPrice.name)
        totalDPCPriceCol.setValueFactory(DataClasses.Report::totalDPCPrice.name)
        totalPriceCol.setValueFactory { (it.totalWorkPrice + it.totalDPCPrice).toString() }
        vatCol.setValueFactory { if (it.vat != null) it.vat.toString() else "-" }
        vatPriceCol.setValueFactory {
            if (it.vat != null) (it.totalWorkPrice + it.totalDPCPrice) * it.vat!! / 100 else "-"
        }
        totalPriceWithVATCol.setValueFactory {
            if (it.vat != null) (it.totalWorkPrice + it.totalDPCPrice) * (it.vat!! / 100 + 1)
            else (it.totalWorkPrice + it.totalDPCPrice)
        }
        workCountCol.setValueFactory(DataClasses.Report::workCount.name)
        dfcCountCol.setValueFactory(DataClasses.Report::dfcCount.name)
        dpcCountCol.setValueFactory(DataClasses.Report::dpcCount.name)
        totalPosCountCol.setValueFactory { (it.workCount + it.dfcCount + it.dpcCount) }

        monthsCb.items = months.toFXList()
        yearCb.items = years.toFXList()
        init(
            LocalDate.now().month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("RU")),
            LocalDate.now().year
        )

        monthsCb.setOnAction { init(monthsCb.value, yearCb.value) }
        yearCb.setOnAction { init(monthsCb.value, yearCb.value) }

        cashTb.setOnAction { init(monthsCb.value, yearCb.value) }
        cashlessTb.setOnAction { init(monthsCb.value, yearCb.value) }

        confirmBtn.setOnAction {
            if (Dialogs.print(
                    confirmBtn.scene.window as Stage,
                    PageOrientation.LANDSCAPE,
                    table
                )
            ) ToolsController.GET_REPORT.update<GetReportController>()
        }

        fillBtn.setOnAction {
            Dialogs.getFile(confirmBtn.scene.window as Stage, File(".").absolutePath, "xlsx" to "Excel")?.let { file ->
                XSSFWorkbook(file).use { wb ->
                    val sheet = wb.getSheet("Выр. (отгрузка)")

                    val reportRow = reports.sortedBy { it.exDate.year * 10000 + it.exDate.month * 100 + it.exDate.day }

                    var rowIndex = 11
                    var reportIndex = 0
                    var currentRow: XSSFRow? = sheet.getRow(rowIndex)

                    while (currentRow?.getCell(1)?.cellType == CellType.NUMERIC) {
                        val cellDate = currentRow.getCell(1)?.dateCellValue ?: continue
                        val date = LocalDate.ofInstant(cellDate.toInstant(), ZoneId.systemDefault())

                        while (reportRow.getOrNull(reportIndex) != null && reportRow[reportIndex].exDate.toLocalDate() < date) {
                            sheet.insertRow(9, rowIndex).fill(reportRow[reportIndex])

                            reportIndex++
                        }

                        rowIndex++
                        currentRow = sheet.getRow(rowIndex)
                    }

                    for (i in reportIndex until reports.size) {
                        sheet.insertRow(9, rowIndex - 1).fill(reportRow[reportIndex])
                    }

                    XSSFFormulaEvaluator.evaluateAllFormulaCells(wb)

                    wb.write(File("filled_report.xlsx").apply { createNewFile() }.outputStream())
                }
            }
        }
    }

    private fun init(month: String, year: Int) {
        val m = month.replaceFirstChar { it.titlecase() }

        val monthIndex = months.indexOf(m)

        table.items =
            reports.filter { it.type == (if (cashTb.isSelected) 0 else 1) && it.exDate.month == monthIndex && it.exDate.year == year }
                .toFXList()

        monthsCb.value = m
        yearCb.value = year
    }

    private fun XSSFSheet.insertRow(srcRow: Int, destRow: Int): XSSFRow {
        shiftRows(destRow, lastRowNum, 1)
        copyRows(srcRow, srcRow, destRow, CellCopyPolicy())

        return getRow(destRow)
    }

    private fun XSSFRow.fill(report: DataClasses.Report) {
        getCell(1).setCellValue(report.exDate.toLocalDate())
        getCell(2).setCellValue(report.customer)
        getCell(3).setCellValue(report.totalDPCPrice + report.totalDPCPrice)
    }
}