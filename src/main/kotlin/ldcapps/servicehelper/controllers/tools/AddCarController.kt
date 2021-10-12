package ldcapps.servicehelper.controllers.tools

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import ldcapps.servicehelper.Dialogs
import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.db.DataClasses.Companion.cars
import ldcapps.servicehelper.db.DataClasses.Companion.companies
import ldcapps.servicehelper.db.DataClasses.Companion.individuals
import ldcapps.servicehelper.db.DataClasses.Companion.owners
import ldcapps.servicehelper.inSize
import ldcapps.servicehelper.isNotNull
import ldcapps.servicehelper.toFXList
import ldclibs.javafx.controls.AutoCompletedDoubleTextField
import ldclibs.javafx.controls.AutoCompletedIntTextField
import ldclibs.javafx.controls.AutoCompletedTextField
import ldclibs.javafx.controls.MyTextField
import ldclibs.javafx.controls.pickers.CarNumberPicker
import java.net.URL
import java.util.*

class AddCarController : Initializable {
    @FXML
    private lateinit var confirmBtn: Button

    @FXML
    private lateinit var customerCb: ComboBox<String>

    @FXML
    private lateinit var ownerTf: AutoCompletedTextField<String>

    @FXML
    private lateinit var individualTf: AutoCompletedTextField<String>

    @FXML
    private lateinit var addressTf: AutoCompletedTextField<String>

    @FXML
    private lateinit var modelTf: AutoCompletedTextField<String>

    @FXML
    private lateinit var vinTf: MyTextField

    @FXML
    private lateinit var yearTf: AutoCompletedIntTextField

    @FXML
    private lateinit var engineTf: AutoCompletedDoubleTextField

    @FXML
    private lateinit var numberPicker: CarNumberPicker

    @FXML
    lateinit var keyTf: TextField

    private fun enable(vararg nodes: Node) =
        nodes.forEach { it.isDisable = false }

    private fun disable(vararg nodes: Node) =
        nodes.forEach { it.isDisable = true }

    private fun changeAction(addCompany: Boolean) {
        if (addCompany) {
            disable(individualTf, addressTf)
            enable(customerCb, ownerTf)
        } else {
            disable(customerCb, ownerTf)
            enable(individualTf, addressTf)
        }
    }

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        modelTf.items = cars.map { it.model }
        yearTf.items = cars.map { it.year }
        engineTf.items = cars.map { it.engine }
        customerCb.items = companies.map { it.company }.toFXList()
        ownerTf.items = owners.map { it.owner }
        individualTf.items = individuals.map { it.individual }

        customerCb.setOnAction { ownerTf.text = customerCb.value }
        customerCb.setOnMouseClicked { changeAction(true) }
        ownerTf.setOnMouseClicked {
            changeAction(true)

            ownerTf.selectAll()
        }

        addressTf.setOnMouseClicked { changeAction(false) }
        individualTf.setOnMouseClicked { changeAction(false) }
        individualTf.onAutoCompleted = { r -> addressTf.text = individuals.find { it.individual == r }!!.address }

        numberPicker.onClose = {
            keyTf.text = numberPicker.number.toString()
        }

        confirmBtn.setOnAction {
            when {
                !isNotNull(modelTf, vinTf, yearTf, engineTf, numberPicker, keyTf) ||
                        inSize(yearTf to 4, vinTf to 17, engineTf to 3, numberPicker to 9) -> return@setOnAction
                cars.map { it.number }.find { it == numberPicker.value } != null -> {
                    Dialogs.warning("Данное авто уже есть в базе данных")
                    return@setOnAction
                }
                cars.map { it.keyNum }.find { it == keyTf.text } != null -> {
                    Dialogs.warning("Данный ключ уже есть в базе данных")
                    return@setOnAction
                }
                isNotNull(customerCb, ownerTf, playAnim = false) &&
                        owners.find { it.owner == ownerTf.text } == null -> {
                    owners.add(DataClasses.Owner(ownerTf.text, customerCb.value))
                }
                isNotNull(individualTf, addressTf, playAnim = false) &&
                        individuals.find { it.individual == individualTf.text } == null ->
                    individuals.add(DataClasses.Individual(individualTf.text, addressTf.text))
            }

            val car = DataClasses.Car(
                numberPicker.value, keyTf.text,
                modelTf.text,
                vinTf.text,
                yearTf.text.toInt(),
                engineTf.text.toDouble(),
                customerCb.value
            )
            cars.add(car)

            Dialogs.information("Авто успешно добавлено")
            Tools.ADD_CAR.update<AddCarController>()

        }
    }
}