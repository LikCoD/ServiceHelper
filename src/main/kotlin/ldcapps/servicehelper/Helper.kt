package ldcapps.servicehelper

import com.google.gson.Gson
import com.ibm.icu.text.RuleBasedNumberFormat
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.control.ComboBox
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import ldcapps.servicehelper.controllers.tools.CreateContract
import ldcapps.servicehelper.controllers.tools.Panes
import ldcapps.servicehelper.controllers.tools.ToolSelector
import ldcapps.servicehelper.db.DataClasses
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

val crypt = Crypt()

fun generateToken(tokenLength: Int = 100): String {
    var res = ""

    for (i in 1..tokenLength)
        res += (('a'..'z') + (1..9)).random()

    return res
}

fun getToken(): String? {
    val token = File("token.key")
    if (token.exists())
        return crypt.decrypt()
    return null
}

fun open(path: String? = null) {
    if (!isOnline || DataClasses.db?.checkToken() == true) {
        val mainController = Windows.ooController
        val extension = path?.substringAfterLast(".")
        val controller =
            if (extension == "oab" || extension == "oo" || extension == null) ToolSelector() else Windows.tools()!!

        if (path != null)
            when (extension) {
                "db" -> Panes.REDACT_DB.show(controller.redactDBTb)
                "report" -> Panes.GET_REPORT.show(controller.getReportTb)
                "settings" -> Panes.SETTINGS.show(controller.settingsTb)
                "contract" -> (Panes.CREATE_CONTRACT.show<CreateContract>(controller.createContractTb)).loadData(path)
                "act" -> Windows.act()?.fill(fromJSON(path), path)
                "oab", "oo" -> mainController?.fillOO(fromJSON(path), path)
                else -> Dialogs.warning("Ошибка инициализации файла")
            }
    } else {
        DataClasses.delete()
        Windows.login()
    }
}

fun <E> List<E>.toFXList(): ObservableList<E> = FXCollections.observableArrayList(this)
fun fxList(vararg el: String): ObservableList<String> = FXCollections.observableArrayList(*el)

fun isNotNull(vararg nodes: Node, playAnim: Boolean = true): Boolean {
    var isNotNull = true

    nodes.forEach {
        if (when (it) {
                is ComboBox<*> -> (it.value as String?)?.trim() ?: ""
                is TextField -> it.text.trim()
                else -> ""
            } == ""
        ) {
            if (playAnim) Animations.emptyNode(it)
            isNotNull = false
        }
    }

    return isNotNull
}

fun inSize(vararg p: Pair<Node, Int>): Boolean {
    var isSize = true
    p.forEach {
        if (when (it.first) {
                is ComboBox<*> -> ((it.first as ComboBox<*>).value as String).trim()
                else -> (it.first as TextField).text.trim()
            }.length != it.second
        ) {
            Animations.emptyNode(it.first)
            isSize = false
        }
    }
    return isSize
}

inline fun <reified T> fromJSON(file: File, textNotExist: String = "{}"): T {
    if (!file.exists()) file.writeText(textNotExist)

    val text = file.readText()
    return Gson().fromJson(text, T::class.java)
}

inline fun <reified T> fromJSON(fileName: String, textNotExist: String = "{}"): T =
    fromJSON(File(fileName), textNotExist)

inline fun <reified T : Any> arrFromJSON(file: String): MutableList<T> = fromJSON<Array<T>>(file, "[]").toMutableList()

fun <T : Any> toJSON(fileName: String, jClass: T) = toJSON(File(fileName), jClass)
fun <T : Any> toJSON(file: File, jClass: T) = file.writeText(Gson().toJson(jClass))

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

fun <T> loadFXML(fxml: FXML): T = fxmlLoader(fxml).load()
fun fxmlLoader(fxml: FXML) = FXMLLoader(Windows::class.java.classLoader.getResource("fxml/${fxml.path}.fxml"))

fun numToStr(n: Double): String {
    val text = RuleBasedNumberFormat(Locale.forLanguageTag("ru"), RuleBasedNumberFormat.SPELLOUT).format(n.toInt())
    val penny = DecimalFormat(".00").format(n).substringAfter(".")

    return "${text.replaceFirstChar { it.titlecase(Locale.getDefault()) }} рублей $penny копеек"
}