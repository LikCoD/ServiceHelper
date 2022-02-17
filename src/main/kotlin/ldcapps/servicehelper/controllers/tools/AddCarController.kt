package ldcapps.servicehelper.controllers.tools

import javafx.application.Platform.runLater
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.TextField
import ldcapps.servicehelper.Animations
import ldcapps.servicehelper.NotNullField
import ldcapps.servicehelper.NotNullField.Companion.check
import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.db.DataClasses.Companion.cars
import ldcapps.servicehelper.db.DataClasses.Companion.companies
import ldcapps.servicehelper.db.DataClasses.Companion.individuals
import ldcapps.servicehelper.db.DataClasses.Companion.owners
import ldclibs.javafx.controls.AutoCompletedDoubleTextField
import ldclibs.javafx.controls.AutoCompletedIntTextField
import ldclibs.javafx.controls.AutoCompletedTextField
import ldclibs.javafx.controls.pickers.CarNumberPicker
import ldclibs.javafx.controls.pickers.VINPicker
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
    private lateinit var customerCb: AutoCompletedTextField<DataClasses.Company>

    @FXML
    @NotNullField("Company")
    private lateinit var ownerTf: AutoCompletedTextField<DataClasses.Owner>

    @FXML
    @NotNullField("Individual")
    private lateinit var individualTf: AutoCompletedTextField<DataClasses.Individual>

    @FXML
    @NotNullField("Individual")
    private lateinit var addressTf: AutoCompletedTextField<String>

    @FXML
    @NotNullField
    private lateinit var modelTf: AutoCompletedTextField<String>

    @FXML
    @NotNullField(size = 17)
    private lateinit var vinTf: VINPicker

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

    private var addCompany = true

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
        customerCb.items = companies
        ownerTf.items = owners
        individualTf.items = individuals

        customerCb.getString = { it.company }
        ownerTf.getString = { it.owner }
        individualTf.getString = { it.individual }

        customerCb.onAutoCompleted = { ownerTf.text = it.company }
        individualTf.onAutoCompleted = { r -> addressTf.text = r.address }

        vinTf.vinInfoList = cars.map { VINPicker.VinInfo(it.vin.substring(3, 9), it.engine, it.model) }

        vinTf.onClose = {
            yearTf.text = vinTf.year.toString()
            engineTf.text = vinTf.selectedInfo?.engine?.toString() ?: ""
            modelTf.text = vinTf.selectedInfo?.model ?: ""
        }

        numberPicker.onClose = {
            keyTf.text = numberPicker.number.toString()
        }

        confirmBtn.setOnAction {
            when {
                !check() -> return@setOnAction
                cars.find { it.number == numberPicker.value } != null -> {
                    Animations.warningNode(numberPicker)
                    return@setOnAction
                }
                cars.find { it.key == keyTf.text } != null -> {
                    Animations.warningNode(keyTf)
                    return@setOnAction
                }
                companies.find { it.company == customerCb.text } == null && addCompany -> {
                    Animations.warningNode(customerCb)
                    return@setOnAction
                }
            }

            when {
                addCompany && !check(type = "Company") && ownerTf.text != customerCb.text -> {
                    if (owners.find { it.owner == ownerTf.text } == null)
                        owners.add(DataClasses.Owner(ownerTf.text, customerCb.selectedItem!!.id))

                    return@setOnAction
                }
                !check(type = "Individual") -> {
                    if (individuals.find { it.individual == individualTf.text } == null)
                        individuals.add(DataClasses.Individual(individualTf.text, addressTf.text))

                    return@setOnAction
                }
            }

            val individualId = individuals.find { it.individual == individualTf.text }?.id
            val companyId = customerCb.selectedItem?.id
            val ownerId = owners.find { it.owner == ownerTf.text }?.id

            if (individualId == null && companyId == null && ownerId == null) return@setOnAction

            val car = DataClasses.Car(
                numberPicker.value,
                keyTf.text,
                modelTf.text,
                vinTf.value,
                yearTf.text.toInt(),
                engineTf.text.toDouble(),
                ownerId,
                companyId,
                individualId
            )

            cars.add(car)

            confirmBtn.text = "Успешно"
            thread {
                Thread.sleep(1000)
                runLater {
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