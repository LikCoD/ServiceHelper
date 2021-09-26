package ldcapps.servicehelper.controllers

import com.google.gson.Gson
import javafx.fxml.Initializable
import javafx.print.PageOrientation
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import javafx.scene.text.Text
import javafx.stage.Stage
import ldcapps.servicehelper.*
import ldcapps.servicehelper.db.DataClasses
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class CreateAct : Initializable {
    lateinit var stage: Stage
    lateinit var confirmBtn: Button
    lateinit var dateP: DatePicker
    lateinit var mileageTf: TextField
    lateinit var reasonTf: TextField
    lateinit var defectTf: TextField
    lateinit var ooLocateBtn: Button
    lateinit var cashTb: ToggleButton
    lateinit var cashlessTb: ToggleButton
    lateinit var ooCb: ComboBox<String>
    lateinit var workCb: ComboBox<String>
    lateinit var detailCb: ComboBox<String>
    lateinit var actAp: AnchorPane
    lateinit var headerTx: Text
    lateinit var reasonTx: Text
    lateinit var diagnosticTx: Text
    lateinit var defectTx: Text
    lateinit var detailTx: Text
    lateinit var companyTx: Text

    private var act = Act()
    private var path = ""

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        dateP.value = LocalDate.now()

        ooCb.items =
            File("${settings.oosLocate}/Безнал").list { _, name -> name.startsWith("Заказ-Наряд") && name.endsWith(".oab") }
                ?.toMutableList()?.toFXList() ?: mutableListOf<String>().toFXList()
        cashTb.setOnAction {
            ooCb.items =
                File("${settings.oosLocate}/Нал").list { _, name -> name.startsWith("Заказ-Наряд") && name.endsWith(".oo") }
                    ?.toMutableList()?.toFXList() ?: mutableListOf<String>().toFXList()
        }
        cashlessTb.setOnAction {
            ooCb.items =
                File("${settings.oosLocate}/Безнал").list { _, name -> name.startsWith("Заказ-Наряд") && name.endsWith(".oab") }
                    ?.toMutableList()?.toFXList() ?: mutableListOf<String>().toFXList()
        }

        ooCb.setOnAction {
            try {
                val path =
                    "${settings.oosLocate}/${if (cashTb.isSelected) "Нал" else "Безнал"}\\${ooCb.value}"
                initOO(fromJSON(path), path)
            } catch (ex: Exception) {
            }
        }

        ooLocateBtn.setOnAction {
            val path = Dialogs.getFile(stage)
            if (path != null) initOO(fromJSON(path), path)
        }

        confirmBtn.setOnAction {
            if (isNotNull(mileageTf, reasonTf, defectTf, ooCb, workCb, detailCb)) {
                try {
                    if (path == "") path = Regex("[/*?\"<>|]").replace(
                        "${settings.actsLocate}\\Акт от ${
                            DateTimeFormatter.ofPattern("dd.MM.yyyy").format(dateP.value)
                        } от ${act.carOwner}.act",
                        ""
                    )
                    FileOutputStream(path).write(Gson().toJson(act).toByteArray())
                    Dialogs.confirmation("Договор успешно создан и находится по пути:\n$path\nРаспечатать его?") {
                        Dialogs.print(stage, PageOrientation.PORTRAIT, actAp)
                    }
                    stage.close()
                } catch (ex: Exception) {
                    Dialogs.warning("Невозможно создать Акт")
                }
            }
        }
    }

    fun initOO(ooAndBill: OOController.OOAndBill, path: String) {
        ooCb.value = path.substringAfterLast("\\")
        with(ooAndBill) {
            act.ooNumber = ooAndBill.number
            workCb.items = ooAndBill.works.map { it.name }.toFXList()
            detailCb.items = (ooAndBill.dfcs.map { it.name } + ooAndBill.dpcs.map { it.name }).toFXList()
            act.abbreviatedFullName = abbreviatedCompanyName
            act.executionDate = executionDate.toString()
            act.carNumber = carNumber
            act.carModel = carModel
            act.carMileage = carMileage
            act.carEngine = carEngine
            act.carYear = carYear
            act.carVIN = carVIN
            act.carOwner = customerOwner
            initAct()
        }
    }

    fun initAct() {
        if (act.isInit())
            with(act) {
                reason = reasonTf.text
                defect = defectTf.text
                work = workCb.editor.text ?: ""
                detail = detailCb.editor.text ?: ""
                date = dateP.editor.text
                carMileage = mileageTf.text
                headerTx.text =
                    "$date на СТО $abbreviatedFullName, расположенным по адресу " +
                            "${DataClasses.user.serviceAddress} поступил автомобиль марки $carModel $carYear года выпуска, VIN " +
                            "$carVIN, гос. номер $carNumber, пробег ${mileageTf.text}, принадлежащий $carOwner."
                reasonTx.text = "Причина обращения - ${reason}."
                diagnosticTx.text = "При диагностике автомобиля установлен дефект ${defect}."
                defectTx.text =
                    "Ранее $executionDate (Заказ-Наряд №$ooNumber) на данном авто производилась ${work}."
                detailTx.text =
                    "Данная деталь (${detail}) подлежит возврату либо замене на аналогичную надлежащего качества."
                companyTx.text = abbreviatedFullName
            }
    }

    fun fill(act: Act, path: String) {
        this.path = path
        this.act = act
        with(act) {
            reasonTf.text = reason
            defectTf.text = defect
            workCb.editor.text = work
            workCb.items = fxList(work)
            detailCb.editor.text = detail
            detailCb.items = fxList(detail)
            dateP.editor.text = date
            mileageTf.text = carMileage
        }
        initAct()
    }

    class Act {
        lateinit var abbreviatedFullName: String
        lateinit var date: String
        lateinit var executionDate: String
        lateinit var reason: String
        lateinit var defect: String
        lateinit var work: String
        lateinit var detail: String
        var ooNumber: Int = 0
        lateinit var carNumber: String
        lateinit var carModel: String
        lateinit var carMileage: String
        lateinit var carEngine: String
        lateinit var carYear: String
        lateinit var carVIN: String
        lateinit var carOwner: String

        fun isInit() = this::carModel.isInitialized
    }
}