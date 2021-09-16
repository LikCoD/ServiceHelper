package ldcapps.servicehelper.controllers

import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.DatePicker
import javafx.scene.image.ImageView
import javafx.scene.text.Text
import ldcapps.servicehelper.*
import ldcapps.servicehelper.controllers.tools.AddCar
import ldcapps.servicehelper.controllers.tools.Panes
import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.db.DataClasses.Companion.cars
import ldcapps.servicehelper.db.DataClasses.Companion.companies
import ldcapps.servicehelper.db.DataClasses.Companion.individuals
import ldcapps.servicehelper.db.DataClasses.Companion.owners
import ldcapps.servicehelper.db.DataClasses.Companion.user
import ldclibs.javafx.controls.AutoCompletedTextField
import ldclibs.javafx.controls.IntTextField
import java.net.URL
import java.sql.Date
import java.time.LocalDate
import java.util.*

class CarData : Initializable {
    lateinit var openBtn: Button
    lateinit var toolsBtn: Button
    lateinit var blankBtn: Button
    lateinit var createActBtn: Button
    lateinit var userTx: Text
    lateinit var userBtn: ImageView
    lateinit var registrationDp: DatePicker
    lateinit var executionDp: DatePicker
    lateinit var keyTf: AutoCompletedTextField
    lateinit var carMileageTf: IntTextField
    lateinit var ooNumberTf: IntTextField
    lateinit var confirmBtn: Button

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        userTx.text = user.name
        userBtn.setOnMouseClicked {
            Panes.SETTINGS.show(Windows.tools()!!.settingsTb)
        }

        keyTf.items = cars.map { it.keyNum }.toFXList()

        registrationDp.value = LocalDate.now()
        executionDp.value = LocalDate.now()

        openBtn.setOnAction {
            Dialogs.getFile(Main.stage, settings.oosLocate)?.let {
                open(it)
            }
        }
        toolsBtn.setOnAction { Windows.tools() }
        createActBtn.setOnAction { Windows.act() }
        blankBtn.setOnAction { Windows.blank() }

        registrationDp.setOnAction { executionDp.value = registrationDp.value }

        confirmBtn.setOnAction { _ ->
            if (isNotNull(keyTf))
                try {
                    cars.find { it.keyNum == keyTf.text }?.let {
                        var address = ""
                        var pa: String? = null
                        var bank: String? = null
                        var bik: String? = null
                        var prn: String? = null
                        var cDate: String? = null
                        val customer = owners.find { o -> o.owner == it.owner }?.company ?: it.owner
                        var number = 0
                        individuals.find { i -> i.individual == customer }?.let { i ->
                            number = Main.lastCashNumber++
                            address = i.address
                        } ?: companies.find { c -> c.company == customer }?.let { c ->
                            number = Main.lastCashlessNumber++
                            address = c.address
                            pa = c.pa
                            bank = c.bank
                            bik = c.bik
                            prn = c.prn
                            cDate = c.contractDate
                        }

                        if (ooNumberTf.text != "")
                            number = ooNumberTf.text.toInt()

                        Windows.ooController?.fill(
                            OO.OOAndBill(
                                number,
                                DataClasses.Date(registrationDp.value),
                                DataClasses.Date(executionDp.value),
                                it.number,
                                it.model,
                                carMileageTf.text,
                                it.engine,
                                it.year,
                                it.vin,
                                it.owner,
                                customer,
                                address,
                                pa,
                                bank,
                                bik,
                                prn,
                                cDate,
                                user.vat,
                                user.hourNorm,
                                user.executor,
                                user.abbreviatedExecutor,
                                user.address,
                                user.pa,
                                user.bank,
                                user.bankAddress,
                                user.bik,
                                user.prn.toString(),
                                user.phone
                            )
                        )
                    }
                        ?: Dialogs.confirmation("Данного гос. номера авто нет в БД, но вы можите добавить его в меню \"Инструменты\"") {
                            Panes.ADD_CAR.show<AddCar>(Windows.tools()!!.addCarTb).keyTf.text = keyTf.text
                        }

                    registrationDp.value = LocalDate.now()
                    executionDp.value = LocalDate.now()
                    keyTf.text = ""
                    carMileageTf.text = ""
                    ooNumberTf.text = ""
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    Dialogs.warning("Невозможно создать Заказ-Наряд")
                }
        }
    }
}

