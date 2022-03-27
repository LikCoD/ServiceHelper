package ldcapps.servicehelper

import com.beust.klaxon.Klaxon
import com.ibm.icu.text.RuleBasedNumberFormat
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXMLLoader
import javafx.scene.control.TableView
import javafx.stage.Stage
import ldcapps.servicehelper.Windows.mainController
import ldcapps.servicehelper.controllers.tools.CreateContractController
import ldcapps.servicehelper.controllers.tools.ToolSelectorController
import ldcapps.servicehelper.controllers.tools.ToolsController
import liklibs.db.Date
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import tornadofx.asObservable
import tornadofx.getProperty
import tornadofx.property
import java.awt.Toolkit
import java.io.File
import java.text.DecimalFormat
import java.time.LocalDate
import java.util.*

data class Settings(
    var oosLocate: String = "",
    var contractsLocate: String = "",
    var actsLocate: String = "",
    var host: String = "",
    var port: Int = 0,
    var login: String = "",
    var password: String = "",
    var dbName: String = "",
)

class Data {
    var works: MutableList<Hint> = mutableListOf()
    var dpcs: MutableList<Hint> = mutableListOf()
    var dfcs: MutableList<Hint> = mutableListOf()
    var bankAddresses: MutableList<String> = mutableListOf()
    var footings: MutableList<String> = mutableListOf()
    var inPersons: MutableList<String> = mutableListOf()
    var banks: MutableList<Bank> = mutableListOf()

    data class Bank(var bik: String = "", var name: String = "", var address: String = "")
    data class Hint(
        val carModel: String,
        val name: String,
        var price: Double? = null,
        var count: Int? = null,
        var state: String? = null,
        var unit: String? = null,
    )
}

class Config(var customers: MutableList<String> = mutableListOf(), var colors: MutableList<String> = mutableListOf())

class Detail(
    var id: Int = 0,
    date: LocalDate = LocalDate.now(),
    car: String = "",
    detail: String = "",
    price: Double = 0.0,
    customer: String = "",
    var type: String = ""
) {
    var date: LocalDate by property(date)
    var car: String by property(car)
    var detail: String by property(detail)
    var price: Double by property(price)
    var customer: String by property(customer)

    fun dateProperty() = getProperty(Detail::date)
    fun carProperty() = getProperty(Detail::car)
    fun detailProperty() = getProperty(Detail::detail)
    fun priceProperty() = getProperty(Detail::price)
    fun customerProperty() = getProperty(Detail::customer)
}

data class DetailND(
    var date: Date,
    var car: String = "",
    var detail: String = "",
    var price: Double = 0.0,
    var customer: String = "",
    var type: String = ""
)

data class SavedDetails(
    val index: Int,
    val details: List<DetailND>
)

var details = mutableListOf<Detail>().asObservable()
var pConfig = fromJSON<Config>(".config")
var settings = fromJSON<Settings>(".settings")
var data = fromJSON<Data>(".data")

fun open(path: String? = null, stage: Stage? = null) {
    if (path == null || stage == null) return

    val extension = path.substringAfterLast(".")
    val controller = if (extension == "oab" || extension == "oo") ToolSelectorController() else Windows.tools ?: return

    when (extension) {
        "db" -> ToolsController.REDACT_DB.show(controller.redactDBTb, stage)
        "report" -> ToolsController.GET_REPORT.show(controller.getReportTb, stage)
        "settings" -> ToolsController.SETTINGS.show(controller.settingsTb, stage)
        "contract" -> (ToolsController.CREATE_CONTRACT.show<CreateContractController>(
            controller.createContractTb,
            stage
        )).loadData(path)
        "act" -> Windows.act()?.fill(fromJSON(path), path)
        "oab", "oo" -> mainController.fillOO(fromJSON(path), path)
        else -> Dialogs.warning("Ошибка инициализации файла")
    }
}

fun <E> Iterable<E>.toFXList(): ObservableList<E> = FXCollections.observableArrayList(this.toList())
fun fxList(vararg el: String): ObservableList<String> = FXCollections.observableArrayList(*el)

inline fun <reified T> fromJSON(file: File): T {
    if (!file.exists()) file.writeText("{}")

    return Klaxon().parse<T>(file.readText()) ?: throw IllegalStateException()
}

inline fun <reified T> fromJSON(fileName: String): T =
    fromJSON(File(fileName))

inline fun <reified T : Any> arrFromJSON(fileName: String): MutableList<T> =
    arrFromJSON(File(fileName))

inline fun <reified T : Any> arrFromJSON(file: File): MutableList<T> {
    if (!file.exists()) file.writeText("[]")

    return Klaxon().parseArray<T>(file.readText())?.toMutableList() ?: throw IllegalStateException()
}


fun <T : Any> toJSON(fileName: String, jClass: T) = toJSON(File(fileName), jClass)
fun <T : Any> toJSON(file: File, jClass: T) {
    file.writeText(Klaxon().toJsonString(jClass))
}

inline fun <reified O> getCellValue(cell: Cell?, defValue: O) = when (cell?.cellType) {
    CellType.NUMERIC -> Converter().toGenerics(cell.numericCellValue) ?: defValue
    CellType.STRING -> Converter().toGenerics(cell.stringCellValue) ?: defValue
    else -> defValue
}

inline fun <reified O> getCellValue(row: Row, cellNumber: Int, defValue: O) =
    getCellValue(row.getCell(cellNumber), defValue)

fun TableView<*>.initTableSize(vararg proportions: Int) {
    var w = Toolkit.getDefaultToolkit().screenSize.width.toDouble() - 340
    val h = Toolkit.getDefaultToolkit().screenSize.height.toDouble() - 100

    prefWidth = if (w < h * (351.0 / 248.0)) w else h * (351.0 / 248.0)
    prefHeight = prefWidth / (351.0 / 248.0)

    w = (prefWidth - 20) / proportions.sum()

    columns.forEachIndexed { i, tableColumn -> tableColumn.prefWidth = proportions[i] * w }
}

fun fxmlLoader(fxmlInfo: FXMLInfo) =
    FXMLLoader(Windows::class.java.classLoader.getResource("fxml/${fxmlInfo.path}.fxml"))

fun numToStr(n: Double): String {
    val text = RuleBasedNumberFormat(Locale.forLanguageTag("ru"), RuleBasedNumberFormat.SPELLOUT).format(n.toInt())
    val penny = DecimalFormat(".00").format(n).substringAfter(".")

    return "${text.replaceFirstChar { it.titlecase(Locale.getDefault()) }} рублей $penny копеек"
}

fun Date.toLocalString() = "$day.$month.$year"