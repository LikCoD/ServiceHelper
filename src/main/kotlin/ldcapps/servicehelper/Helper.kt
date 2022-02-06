package ldcapps.servicehelper

import com.ibm.icu.text.RuleBasedNumberFormat
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXMLLoader
import javafx.scene.control.TableView
import javafx.stage.Stage
import kotlinx.serialization.ExperimentalSerializationApi
import ldcapps.servicehelper.controllers.tools.CreateContractController
import ldcapps.servicehelper.controllers.tools.ToolSelectorController
import ldcapps.servicehelper.controllers.tools.ToolsController
import liklibs.db.Date
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import java.awt.Toolkit
import java.io.File
import java.text.DecimalFormat
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
    var works: MutableSet<Hint> = mutableSetOf()
    var dpcs: MutableSet<Hint> = mutableSetOf()
    var dfcs: MutableSet<Hint> = mutableSetOf()
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

var settings = fromJSON<Settings>(".settings")
var data = fromJSON<Data>(".data")

@ExperimentalSerializationApi
fun open(path: String? = null, stage: Stage? = null) {
    val mainController = Windows.ooController
    val extension = path?.substringAfterLast(".")
    val controller =
        if (extension == "oab" || extension == "oo" || extension == null) ToolSelectorController() else Windows.tools()!!

    if (path == null || stage == null) return

    when (extension) {
        "db" -> ToolsController.REDACT_DB.show(controller.redactDBTb, stage)
        "report" -> ToolsController.GET_REPORT.show(controller.getReportTb, stage)
        "settings" -> ToolsController.SETTINGS.show(controller.settingsTb, stage)
        "contract" -> (ToolsController.CREATE_CONTRACT.show<CreateContractController>(
            controller.createContractTb,
            stage
        )).loadData(path)
        "act" -> Windows.act()?.fill(fromJSON(path), path)
        "oab", "oo" -> mainController?.fillOO(fromJSON(path), path)
        else -> Dialogs.warning("Ошибка инициализации файла")
    }
}

fun <E> Iterable<E>.toFXList(): ObservableList<E> = FXCollections.observableArrayList(this.toList())
fun fxList(vararg el: String): ObservableList<String> = FXCollections.observableArrayList(*el)

inline fun <reified T> fromJSON(file: File, textNotExist: String = "{}"): T {
    if (!file.exists()) file.writeText(textNotExist)

    val json = file.readText()
    return Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(T::class.java).fromJson(json)!!
}

inline fun <reified T> fromJSON(fileName: String, textNotExist: String = "{}"): T =
    fromJSON(File(fileName), textNotExist)

inline fun <reified T : Any> arrFromJSON(fileName: String): MutableList<T> {
    val file = File(fileName)
    if (!file.exists()) file.writeText("[]")

    val json = file.readText()
    return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        .adapter<MutableList<T>>(Types.newParameterizedType(MutableList::class.java, T::class.java)).fromJson(json)!!
}

fun <T : Any> toJSON(fileName: String, jClass: T) = toJSON(File(fileName), jClass)
fun <T : Any> toJSON(file: File, jClass: T) {
    val json = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter<T>(jClass::class.java).toJson(jClass)
    file.writeText(json)
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

@ExperimentalSerializationApi
fun <T> loadFXML(fxmlInfo: FXMLInfo): T = fxmlLoader(fxmlInfo).load()

@ExperimentalSerializationApi
fun fxmlLoader(fxmlInfo: FXMLInfo) =
    FXMLLoader(Windows::class.java.classLoader.getResource("fxml/${fxmlInfo.path}.fxml"))

fun numToStr(n: Double): String {
    val text = RuleBasedNumberFormat(Locale.forLanguageTag("ru"), RuleBasedNumberFormat.SPELLOUT).format(n.toInt())
    val penny = DecimalFormat(".00").format(n).substringAfter(".")

    return "${text.replaceFirstChar { it.titlecase(Locale.getDefault()) }} рублей $penny копеек"
}

fun Date.toLocalString() = "$day.$month.$year"