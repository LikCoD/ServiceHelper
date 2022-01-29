package ldcapps.servicehelper.controllers.tools

import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.stage.Stage
import kotlinx.serialization.ExperimentalSerializationApi
import ldcapps.servicehelper.*
import ldcapps.servicehelper.NotNullField.Companion.check
import ldcapps.servicehelper.db.DataClasses.Companion.user
import ldclibs.javafx.controls.DoubleTextField
import ldclibs.javafx.controls.pickers.BankPicker
import ldclibs.javafx.controls.pickers.PRNPicker
import ldclibs.javafx.controls.pickers.PhonePicker
import java.io.File
import java.net.URL
import java.util.*

@ExperimentalSerializationApi
class SignupController : Initializable {
    @NotNullField
    lateinit var oosLocateTf: TextField
    lateinit var oosLocateBtn: Button

    @NotNullField
    lateinit var actsLocateTf: TextField
    lateinit var actsLocateBtn: Button

    @NotNullField
    lateinit var contractsLocateTf: TextField
    lateinit var contractsLocateBtn: Button

    @NotNullField("SingUp")
    lateinit var loginTf: TextField

    @NotNullField("SingUp")
    lateinit var passwordTf: PasswordField

    @NotNullField("SingUp")
    lateinit var passwordRepeatTf: PasswordField
    lateinit var stayOfflineBtn: RadioButton

    @NotNullField
    lateinit var nameTf: TextField
    lateinit var confirmBtn: Button

    @NotNullField
    lateinit var hourNormTf: DoubleTextField

    @NotNullField("Worker")
    lateinit var workerTf: TextField
    lateinit var addWorkerBtn: Button
    lateinit var vatTf: DoubleTextField
    lateinit var workersCb: ComboBox<String>
    lateinit var deleteWorkerBtn: Button
    lateinit var standardUnitCb: ComboBox<String>
    lateinit var standardStateCb: ComboBox<String>
    lateinit var standardWorkerCb: ComboBox<String>

    @NotNullField
    lateinit var companyNameTf: TextField
    lateinit var serviceAddressTf: TextField

    @NotNullField
    lateinit var bankPicker: BankPicker

    @NotNullField
    lateinit var prnPicker: PRNPicker

    @NotNullField
    lateinit var phoneTf: PhonePicker

    @NotNullField
    lateinit var emailTf: TextField

    @NotNullField
    lateinit var certificateOfStateRegistrationTf: TextField

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        oosLocateTf.text = settings.oosLocate
        actsLocateTf.text = settings.actsLocate
        contractsLocateTf.text = settings.contractsLocate

        oosLocateBtn.setOnAction { oosLocateTf.text = Dialogs.getDirectory(stage, oosLocateTf.text) }
        actsLocateBtn.setOnAction { actsLocateTf.text = Dialogs.getDirectory(stage, actsLocateTf.text) }
        contractsLocateBtn.setOnAction { contractsLocateTf.text = Dialogs.getDirectory(stage, contractsLocateTf.text) }

        val userFile = File(".user")
        val workers = mutableListOf<String>()

        nameTf.text = user.name
        stayOfflineBtn.isSelected = userFile.exists() && userFile.readText() != "{}"
        hourNormTf.text = user.hourNorm.toString()
        vatTf.text = user.vat?.toString() ?: ""
        companyNameTf.text = user.executor
        serviceAddressTf.text = user.serviceAddress
        bankPicker.pa = user.pa
        bankPicker.bank = user.bank
        bankPicker.bankAddress = user.bankAddress
        bankPicker.bik = user.bik
        prnPicker.company = user.abbreviatedExecutor
        prnPicker.address = user.address
        prnPicker.prn = user.prn
        phoneTf.phone = user.phone
        emailTf.text = user.email
        certificateOfStateRegistrationTf.text = user.footing
        standardUnitCb.items = fxList("ш", "л")
        standardUnitCb.value = user.standardUnit
        standardStateCb.items = fxList("н", "б")
        standardStateCb.value = user.standardState
        standardWorkerCb.items = user.workers.toFXList()
        standardWorkerCb.value = user.standardWorker
        workersCb.items = user.workers.toFXList()
        workersCb.value = ""

        addWorkerBtn.setOnAction {
            if (check(type = "Worker")) {
                workers.add(workerTf.text)
                workersCb.items = workers.toFXList()
                standardWorkerCb.items = workers.toFXList()
                workerTf.text = ""
            }
        }

        deleteWorkerBtn.setOnAction {
            if (check(type = "Worker")) {
                workers.remove(workersCb.value)
                workersCb.items = workers.toFXList()
                standardWorkerCb.items = workers.toFXList()
            }
        }

        confirmBtn.setOnAction {
            if (check() && (type == Type.SETTINGS || check(type = "SingUp"))) {
                if (passwordTf.text == passwordRepeatTf.text)
                    try {
                        File("${oosLocateTf.text}/Нал").mkdirs()
                        File("${oosLocateTf.text}/Безнал").mkdirs()
                        File(actsLocateTf.text).mkdirs()
                        File(contractsLocateTf.text).mkdirs()

                        settings = Settings(oosLocateTf.text, contractsLocateTf.text, actsLocateTf.text)
                        toJSON(".settings", settings)

                        if (type != Type.SETTINGS) {
                            //TODO save
                        } else Dialogs.warning("Пароль введен неправильно")
                    } catch (ex: Exception) {
                        Animations.errorButton(confirmBtn, "Ошибка")
                        ex.printStackTrace()
                    }
                else {
                    Animations.emptyNode(passwordTf)
                    Animations.emptyNode(passwordRepeatTf)
                }
            }
        }
    }

    companion object {
        var type = Type.SETTINGS
        var stage = Stage()

        enum class Type {
            SETTINGS,
            SING_UP
        }
    }
}