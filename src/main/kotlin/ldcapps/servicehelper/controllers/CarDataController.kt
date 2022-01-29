package ldcapps.servicehelper.controllers

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.DatePicker
import javafx.scene.image.ImageView
import javafx.scene.text.Text
import javafx.stage.Stage
import kotlinx.serialization.ExperimentalSerializationApi
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
import liklibs.db.toSQL
import java.net.URL
import java.time.LocalDate
import java.util.*

@ExperimentalSerializationApi
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
    private lateinit var keyTf: AutoCompletedTextField<DataClasses.Car>

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

        keyTf.items = cars

        keyTf.getString = { it.key }

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
                val car = keyTf.selectedItem ?: let {
                    Dialogs.confirmation("Данного гос. номера авто нет в БД, но вы можите добавить его в меню \"Инструменты\"") {
                        ToolsController.ADD_CAR.show<AddCarController>(
                            Windows.tools()!!.addCarTb, confirmBtn.scene.window
                        ).keyTf.text = keyTf.text
                    }
                    return@setOnAction
                }

                val ooAndBill = OOController.OOAndBill(
                    registrationDate = registrationDp.value.toSQL(),
                    executionDate = executionDp.value.toSQL(),
                    car = car,
                    carMileage = carMileageTf.text.toIntOrNull(),
                    executor = user.getExecutor()
                )


                val company = companies
                    .find { it.id == (owners.find { o -> o.id == car.ownerId }?.companyId ?: car.companyId) }

                company?.let {
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

