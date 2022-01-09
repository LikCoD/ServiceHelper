package ldcapps.servicehelper.controllers.tools

import javafx.application.Platform.runLater
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import ldcapps.servicehelper.Animations
import ldcapps.servicehelper.NotNullField
import ldcapps.servicehelper.NotNullField.Companion.check
import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.db.DataClasses.Companion.cars
import ldcapps.servicehelper.db.DataClasses.Companion.companies
import ldcapps.servicehelper.db.DataClasses.Companion.individuals
import ldcapps.servicehelper.db.DataClasses.Companion.owners
import ldcapps.servicehelper.toFXList
import ldclibs.javafx.controls.AutoCompletedDoubleTextField
import ldclibs.javafx.controls.AutoCompletedIntTextField
import ldclibs.javafx.controls.AutoCompletedTextField
import ldclibs.javafx.controls.MyTextField
import ldclibs.javafx.controls.pickers.CarNumberPicker
import java.net.URL
import java.util.*
import kotlin.concurrent.thread

class AddCarController : Initializable {
    @FXML
    private lateinit var confirmBtn: Button

    @FXML
    private lateinit var changeTypeBtn: Button

    @FXML
    @NotNullField("Company")
    private lateinit var customerCb: ComboBox<String>

    @FXML
    @NotNullField("Company")
    private lateinit var ownerTf: AutoCompletedTextField<String>

    @FXML
    @NotNullField("Individual")
    private lateinit var individualTf: AutoCompletedTextField<String>

    @FXML
    @NotNullField("Individual")
    private lateinit var addressTf: AutoCompletedTextField<String>

    @FXML
    @NotNullField
    private lateinit var modelTf: AutoCompletedTextField<String>

    @FXML
    @NotNullField(size = 17)
    private lateinit var vinTf: MyTextField

    @FXML
    @NotNullField(size = 4)
    private lateinit var yearTf: AutoCompletedIntTextField

    @FXML
    @NotNullField(size = 3)
    private lateinit var engineTf: AutoCompletedDoubleTextField

    @FXML
    @NotNullField(size = 9)
    private lateinit var numberPicker: CarNumberPicker

    @FXML
    @NotNullField
    lateinit var keyTf: TextField

    var addCompany = true

    private fun enable(vararg nodes: Node) =
        nodes.forEach { it.isDisable = false }

    private fun disable(vararg nodes: Node) =
        nodes.forEach { it.isDisable = true }

    private fun changeType() {
        addCompany = !addCompany
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
        individualTf.onAutoCompleted = { r -> addressTf.text = individuals.find { it.individual == r }!!.address }

        numberPicker.onClose = {
            keyTf.text = numberPicker.number.toString()
        }

        confirmBtn.setOnAction {
            when {
                !check() -> return@setOnAction
                cars.map { it.number }.find { it == numberPicker.value } != null -> {
                    Animations.warningNode(numberPicker)
                    return@setOnAction
                }
                cars.map { it.keyNum }.find { it == keyTf.text } != null -> {
                    Animations.warningNode(keyTf)
                    return@setOnAction
                }
            }

            when {
                check(false, "Company") ->
                    if (owners.find { it.owner == ownerTf.text } == null)
                        owners.add(DataClasses.Owner(ownerTf.text, customerCb.value))
                check(false, "Individual") ->
                    if (individuals.find { it.individual == individualTf.text } == null)
                        individuals.add(DataClasses.Individual(individualTf.text, addressTf.text))
                else -> {
                    check(type = "Company")
                    check(type = "Individual")
                    return@setOnAction
                }
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

            confirmBtn.text = "Успешно"
            thread {
                Thread.sleep(1000)
                runLater{
                    ToolsController.ADD_CAR.update<AddCarController>()
                }
            }
        }

        changeTypeBtn.setOnAction {
            changeType()
            changeTypeBtn.text = if (addCompany) "Компания" else "Физ. лицо"
        }
    }
}