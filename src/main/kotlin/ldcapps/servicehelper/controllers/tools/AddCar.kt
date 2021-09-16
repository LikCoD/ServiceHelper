package ldcapps.servicehelper.controllers.tools

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

class AddCar : Initializable {
    lateinit var confirmBtn: Button
    lateinit var customerCb: ComboBox<String>
    lateinit var ownerTf: AutoCompletedTextField
    lateinit var individualTf: AutoCompletedTextField
    lateinit var addressTf: AutoCompletedTextField
    lateinit var modelTf: AutoCompletedTextField
    lateinit var vinTf: MyTextField
    lateinit var yearTf: AutoCompletedIntTextField
    lateinit var engineTf: AutoCompletedDoubleTextField
    lateinit var numberPicker: CarNumberPicker
    lateinit var keyTf: TextField

    private val disableClass =
        "-fx-background-color: rgba(0,0,0,0.2); -fx-border-width: 1; -fx-border-color: rgb(180,180,180)"

    private fun Node.disableClass() {
        style += disableClass
    }

    private fun enable() =
        arrayListOf(customerCb, ownerTf, individualTf, addressTf).forEach {
            it.style = it.style.replace(disableClass, "")
        }

    private fun disable(vararg nodes: Node) {
        enable()
        nodes.forEach { it.disableClass() }
    }

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        disable(individualTf, addressTf)

        modelTf.items = cars.map { it.model }
        yearTf.items = cars.map { it.year }
        engineTf.items = cars.map { it.engine }
        customerCb.items = companies.map { it.company }.toFXList()
        ownerTf.items = owners.map { it.owner }
        individualTf.items = individuals.map { it.individual }

        customerCb.setOnAction { ownerTf.text = customerCb.value }
        customerCb.setOnMouseClicked { disable(individualTf, addressTf) }
        ownerTf.setOnMouseClicked {
            disable(individualTf, addressTf)

            ownerTf.selectAll()
        }

        individualTf.setOnMouseClicked { disable(customerCb, ownerTf) }
        individualTf.onAutoCompleted = { r -> addressTf.text = individuals.find { it.individual == r }!!.address }
        addressTf.setOnMouseClicked { disable(customerCb, ownerTf) }

        numberPicker.onClose = {
            keyTf.text = numberPicker.number.toString()
        }

        confirmBtn.setOnAction {
            if (isNotNull(
                    customerCb, modelTf, vinTf, yearTf, engineTf, numberPicker, keyTf, ownerTf
                ) && inSize(yearTf to 4, vinTf to 17, engineTf to 3, numberPicker to 9)
            )
                if (cars.map { it.number }.find { it == numberPicker.value } == null)
                    if (cars.map { it.keyNum }.find { it == keyTf.text } == null) {
                        if (customerCb.value != ownerTf.text) {
                            if (ownerTf.style.contains(disableClass) && owners.find { it.owner == ownerTf.text } == null)
                                owners.add(DataClasses.Owner(ownerTf.text, customerCb.value))
                            if (!ownerTf.style.contains(disableClass) && individuals.find { it.individual == ownerTf.text } == null)
                                individuals.add(DataClasses.Individual(customerCb.value, ownerTf.text))
                        }

                        cars.add(
                            DataClasses.Car(
                                numberPicker.value, keyTf.text, modelTf.text,
                                vinTf.text, yearTf.text, engineTf.text, customerCb.value
                            )
                        )

                        Dialogs.information("Авто успешно добавлено")
                        Panes.ADD_CAR.update<AddCar>()
                    } else Dialogs.warning("Данный ключ уже есть в базе данных")
                else Dialogs.warning("Данное авто уже есть в базе данных")
        }
    }
}