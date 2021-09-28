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
import java.time.LocalDate
import java.util.*

class CarDataController : Initializable {
    lateinit var openBtn: Button
    lateinit var toolsBtn: Button
    lateinit var blankBtn: Button
    lateinit var createActBtn: Button
    lateinit var userTx: Text
    lateinit var userBtn: ImageView
    lateinit var registrationDp: DatePicker
    lateinit var executionDp: DatePicker
    lateinit var keyTf: AutoCompletedTextField<String>
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
            open(Dialogs.getFile(MainController.stage, settings.oosLocate))
        }

        toolsBtn.setOnAction { Windows.tools() }
        createActBtn.setOnAction { Windows.act() }
        blankBtn.setOnAction { Windows.blank() }

        registrationDp.setOnAction { executionDp.value = registrationDp.value }

        confirmBtn.setOnAction { _ ->
            if (keyTf.text.isBlank()) return@setOnAction
            try {
                val car = cars.find { it.keyNum == keyTf.text }
                if (car == null) {
                    Dialogs.confirmation("Данного гос. номера авто нет в БД, но вы можите добавить его в меню \"Инструменты\"") {
                        Panes.ADD_CAR.show<AddCar>(Windows.tools()!!.addCarTb).keyTf.text = keyTf.text
                    }
                    return@setOnAction
                }

                val ooAndBill = OOController.OOAndBill(
                    registrationDate = DataClasses.Date(registrationDp.value),
                    executionDate = DataClasses.Date(executionDp.value),
                    car = car,
                    carMileage = carMileageTf.text.toIntOrNull(),
                    customer = owners.find { o -> o.owner == car.owner }?.company ?: car.owner,
                    vat = user.vat,
                    hourNorm = user.hourNorm,
                    companyName = user.executor,
                    abbreviatedCompanyName = user.abbreviatedExecutor,
                    companyAddress = user.address,
                    companyPA = user.pa,
                    companyBank = user.bank,
                    companyBankAddress = user.bankAddress,
                    companyBIK = user.bik,
                    companyPRN = user.prn.toString(),
                    companyPhone = user.phone
                )

                companies.find { c -> c.company == ooAndBill.customer }?.let { c ->
                    ooAndBill.number = MainController.currentCashlessNumber++

                    ooAndBill.customerAddress = c.address
                    ooAndBill.customerPA = c.pa
                    ooAndBill.customerBank = c.bank
                    ooAndBill.customerBIK = c.bik
                    ooAndBill.customerPRN = c.prn
                    ooAndBill.customerContractDate = c.contractDate
                } ?: individuals.find { i -> i.individual == ooAndBill.customer }?.let { i ->
                    ooAndBill.number = MainController.currentCashNumber++

                    ooAndBill.customerAddress = i.address
                }

                if (ooNumberTf.text.isNotBlank())
                    ooAndBill.number = ooNumberTf.text.toInt()

                Windows.ooController?.fillOO(ooAndBill)

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

