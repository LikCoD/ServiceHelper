package ldcapps.servicehelper.controllers.tools

import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.stage.Stage
import ldcapps.servicehelper.*
import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.db.DataClasses.Companion.db
import ldcapps.servicehelper.db.DataClasses.Companion.user
import ldcapps.servicehelper.db.MySqlDb
import ldclibs.javafx.controls.DoubleTextField
import ldclibs.javafx.controls.pickers.BankPicker
import ldclibs.javafx.controls.pickers.PRNPicker
import ldclibs.javafx.controls.pickers.PhonePicker
import java.io.File
import java.net.URL
import java.util.*

class Signup : Initializable {
    lateinit var oosLocateTf: TextField
    lateinit var oosLocateBtn: Button
    lateinit var actsLocateTf: TextField
    lateinit var actsLocateBtn: Button
    lateinit var contractsLocateTf: TextField
    lateinit var contractsLocateBtn: Button
    lateinit var loginTf: TextField
    lateinit var passwordTf: PasswordField
    lateinit var passwordRepeatTf: PasswordField
    lateinit var stayOfflineBtn: RadioButton
    lateinit var nameTf: TextField
    lateinit var confirmBtn: Button
    lateinit var hourNormTf: DoubleTextField
    lateinit var workerTf: TextField
    lateinit var addWorkerBtn: Button
    lateinit var vatTf: DoubleTextField
    lateinit var workersCb: ComboBox<String>
    lateinit var deleteWorkerBtn: Button
    lateinit var standardUnitCb: ComboBox<String>
    lateinit var standardStateCb: ComboBox<String>
    lateinit var standardWorkerCb: ComboBox<String>
    lateinit var companyNameTf: TextField
    lateinit var serviceAddressTf: TextField
    lateinit var bankPicker: BankPicker
    lateinit var prnPicker: PRNPicker
    lateinit var phoneTf: PhonePicker
    lateinit var emailTf: TextField
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
            if (isNotNull(workerTf)) {
                workers.add(workerTf.text)
                workersCb.items = workers.toFXList()
                standardWorkerCb.items = workers.toFXList()
                workerTf.text = ""
            }
        }

        deleteWorkerBtn.setOnAction {
            if (isNotNull(workerTf)) {
                workers.remove(workersCb.value)
                workersCb.items = workers.toFXList()
                standardWorkerCb.items = workers.toFXList()
            }
        }

        confirmBtn.setOnAction {
            if (isNotNull(
                    hourNormTf, companyNameTf, bankPicker, prnPicker,
                    phoneTf, emailTf, certificateOfStateRegistrationTf, hourNormTf,
                    oosLocateTf, contractsLocateTf, actsLocateTf, nameTf
                ) && (type == Type.SETTINGS || isNotNull(loginTf, passwordTf, passwordRepeatTf, playAnim = false))
            ) {
                if (passwordTf.text == passwordRepeatTf.text)
                    try {
                        File("${oosLocateTf.text}/Нал").mkdirs()
                        File("${oosLocateTf.text}/Безнал").mkdirs()
                        File(actsLocateTf.text).mkdirs()
                        File(contractsLocateTf.text).mkdirs()

                        settings = Settings(oosLocateTf.text, contractsLocateTf.text, actsLocateTf.text)
                        toJSON(".settings", settings)

                        if (type != Type.SETTINGS || Dialogs.password { db?.checkPassword(it) == true })
                            if (isOnline) {
                                user = DataClasses.User(
                                    nameTf.text,
                                    hourNormTf.text.toDouble(),
                                    vatTf.text.toDoubleOrNull(),
                                    standardUnitCb.value,
                                    standardStateCb.value,
                                    standardWorkerCb.value,
                                    companyNameTf.text,
                                    prnPicker.company,
                                    prnPicker.address,
                                    serviceAddressTf.text,
                                    bankPicker.pa,
                                    bankPicker.bank,
                                    bankPicker.bankAddress,
                                    prnPicker.prn!!.toInt(),
                                    bankPicker.bik,
                                    phoneTf.phone,
                                    emailTf.text,
                                    certificateOfStateRegistrationTf.text,
                                    workers,
                                )

                                if (type == Type.SETTINGS) {
                                    if (isNotNull(
                                            loginTf, passwordTf, passwordRepeatTf, playAnim = false
                                        ) && passwordTf.text == passwordRepeatTf.text
                                    )
                                        db?.saveUser(user, loginTf.text, passwordTf.text)
                                    else db?.saveUser(user)

                                    Dialogs.information("Настройки успешно изменены")

                                    Windows.ooController
                                } else
                                    when (val res = db?.addUser(user, loginTf.text, passwordTf.text)) {
                                        MySqlDb.Status.NORMAL -> {
                                            Windows.ooController

                                            stage.close()
                                        }
                                        else -> Dialogs.warning(res?.text ?: "")
                                    }

                                if (stayOfflineBtn.isSelected) toJSON(".user", user)
                            } else Dialogs.warning("Необходимо подключение к сети интернет")
                        else Dialogs.warning("Пароль введен неправильно")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        Dialogs.warning("Невозможно сохранить настройки")
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