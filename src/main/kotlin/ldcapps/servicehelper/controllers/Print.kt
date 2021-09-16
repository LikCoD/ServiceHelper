package ldcapps.servicehelper.controllers

import javafx.fxml.Initializable
import javafx.print.PageOrientation
import javafx.print.Paper
import javafx.print.PrintSides
import javafx.print.Printer
import javafx.scene.control.*
import javafx.stage.Stage
import ldcapps.servicehelper.toFXList
import java.net.URL
import java.util.*

class Print : Initializable {
    lateinit var stage: Stage
    lateinit var printerCb: ComboBox<Printer>
    lateinit var paperCb: ComboBox<String>
    lateinit var copiesSpn: Spinner<Int>
    lateinit var solveToCopiesCb: CheckBox
    lateinit var orientationCb: ComboBox<String>
    lateinit var printAreaCb: ComboBox<String>
    lateinit var fieldsCb: ComboBox<String>
    lateinit var printTypeCb: ComboBox<String>
    lateinit var confirmBtn: Button

    var paper: Paper = Paper.A4
    var printer: Printer = Printer.getDefaultPrinter()
    var copies: Int = 2
    var printArea: Int = 0
    var confirmPrint: Boolean = false
    var fields = Printer.MarginType.EQUAL
    var printType = PrintSides.ONE_SIDED

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        printerCb.value = Printer.getDefaultPrinter()
        printerCb.items = Printer.getAllPrinters().reversed().toFXList()
        orientationCb.value = if (pageOrientation == PageOrientation.LANDSCAPE) "Альбомная" else "Портретная"
        copiesSpn.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 2)
        confirmBtn.setOnAction {
            printer = printerCb.value
            fields = when (fieldsCb.value) {
                "Узкие" -> Printer.MarginType.EQUAL_OPPOSITES
                "Обычные" -> Printer.MarginType.HARDWARE_MINIMUM
                else -> Printer.MarginType.DEFAULT
            }
            pageOrientation = when(orientationCb.value){
                "Альбомная" -> PageOrientation.LANDSCAPE
                "Портретная" -> PageOrientation.PORTRAIT
                "Альбомная перевернутая" -> PageOrientation.REVERSE_LANDSCAPE
                "Портретная перевернутая" -> PageOrientation.REVERSE_PORTRAIT
                else -> pageOrientation
            }
            paper = when (paperCb.value) {
                "A0" -> Paper.A0
                "A1" -> Paper.A1
                "A2" -> Paper.A2
                "A3" -> Paper.A3
                "A4" -> Paper.A4
                "A5" -> Paper.A5
                else -> Paper.A6
            }
            printType = when (printTypeCb.value) {
                "Односторонняя" -> PrintSides.ONE_SIDED
                "Двухсторонняя портретная" -> PrintSides.DUPLEX
                else -> PrintSides.TUMBLE
            }
            copies = copiesSpn.value
            printArea = when (printAreaCb.value) {
                "Выделенная область" -> 0
                else -> 1
            }
            confirmPrint = true
            stage.close()
        }
    }

    companion object {
        lateinit var pageOrientation: PageOrientation
    }
}