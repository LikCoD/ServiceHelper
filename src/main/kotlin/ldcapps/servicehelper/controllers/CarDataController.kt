package ldcapps.servicehelper.controllers

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.DatePicker
import javafx.scene.image.ImageView
import javafx.scene.text.Text
import javafx.stage.Stage
import ldcapps.servicehelper.*
import ldcapps.servicehelper.NotNullField.Companion.check
import ldcapps.servicehelper.controllers.tools.AddCarController
import ldcapps.servicehelper.controllers.tools.ToolsController
import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.db.DataClasses.Companion.cars
import ldcapps.servicehelper.db.DataClasses.Companion.companies
import ldcapps.servicehelper.db.DataClasses.Companion.owners
import ldcapps.servicehelper.db.DataClasses.Companion.user
import ldclibs.javafx.controls.AutoCompletedTextField
import ldclibs.javafx.controls.IntTextField
import java.net.URL
import java.time.LocalDate
import java.util.*
import kotlin.concurrent.thread

class CarDataController : Initializable {
    @FXML
    private lateinit var openBtn: Button
    @FXML
    private lateinit var toolsBtn: Button
    @FXML
    private lateinit var blankBtn: Button
    @FXML
    private lateinit var createActBtn: Button
    @FXML
    private lateinit var userTx: Text
    @FXML
    private lateinit var userBtn: ImageView
    @FXML
    @NotNullField
    private lateinit var registrationDp: DatePicker
    @FXML
    @NotNullField
    private lateinit var executionDp: DatePicker
    @FXML
    @NotNullField
    private lateinit var keyTf: AutoCompletedTextField<String>
    @FXML
    private lateinit var carMileageTf: IntTextField
    @FXML
    private lateinit var ooNumberTf: IntTextField
    @FXML
    private lateinit var confirmBtn: Button

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        userTx.text = user.name
        userBtn.setOnMouseClicked {
            val toolsController = Windows.tools()!!
            ToolsController.SETTINGS.show(toolsController.settingsTb, toolsController.stage)
        }

        keyTf.items = cars.map { it.keyNum }.toFXList()

        registrationDp.value = LocalDate.now()
        executionDp.value = LocalDate.now()

        openBtn.setOnAction {
            open(Dialogs.getFile(openBtn.scene.window as Stage, settings.oosLocate), confirmBtn.scene.window as Stage)
        }

        toolsBtn.setOnAction { Windows.tools() }
        createActBtn.setOnAction { Windows.act() }
        blankBtn.setOnAction { Windows.blank() }

        registrationDp.setOnAction { executionDp.value = registrationDp.value }

        confirmBtn.setOnAction { _ ->
            if (!check()) return@setOnAction

            try {
                val car = cars.find { it.keyNum == keyTf.text } ?: let {
                    Dialogs.confirmation("Данного гос. номера авто нет в БД, но вы можите добавить его в меню \"Инструменты\"") {
                        ToolsController.ADD_CAR.show<AddCarController>(
                            Windows.tools()!!.addCarTb, confirmBtn.scene.window
                        ).keyTf.text = keyTf.text
                    }
                    return@setOnAction
                }

                val ooAndBill = OOController.OOAndBill(
                    registrationDate = DataClasses.Date(registrationDp.value),
                    executionDate = DataClasses.Date(executionDp.value),
                    car = car,
                    carMileage = carMileageTf.text.toIntOrNull(),
                    executor = user.getExecutor()
                )

                val company = owners.find { it.owner == car.owner }?.company ?: car.owner
                companies.find { it.company == company }?.let {
                    ooAndBill.number = MainController.currentCashlessNumber
                    MainController.currentCashlessNumber++
                } ?: let {
                    ooAndBill.number = MainController.currentCashNumber
                    MainController.currentCashNumber++
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
                Animations.errorButton(confirmBtn, "Ошибка")
                ex.printStackTrace()
            }
        }
    }
}

