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
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.net.URL
import java.time.LocalDate
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
            TODO()
            Dialogs.getFile(confirmBtn.scene.window as Stage, null, "xlsx" to "Excel")?.let { file ->
                val wb = XSSFWorkbook(File(file))
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
}