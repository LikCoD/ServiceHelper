package ldcapps.servicehelper.controllers

import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.stage.Stage
import ldcapps.servicehelper.*
import ldcapps.servicehelper.NotNullField.Companion.check
import ldclibs.javafx.controls.IntTextField
import ldclibs.javafx.controls.MyTextField
import java.net.URL
import java.util.*

class ChangeDBHostController : Initializable {
    lateinit var stage: Stage
    @NotNullField
    lateinit var hostTf: MyTextField
    @NotNullField(size = 4)
    lateinit var portTf: IntTextField
    @NotNullField
    lateinit var loginTf: MyTextField
    @NotNullField
    lateinit var passwordTf: MyTextField
    @NotNullField
    lateinit var dbNameTf: MyTextField
    lateinit var confirmBtn: Button

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        hostTf.text = settings.host
        portTf.text = settings.port.toString()
        loginTf.text = settings.login
        passwordTf.text = settings.password
        dbNameTf.text = settings.dbName

        confirmBtn.setOnAction {
            if (check()){
                settings.host = hostTf.text.trim()
                settings.port = portTf.text.trim().toInt()
                settings.login = loginTf.text.trim()
                settings.password = passwordTf.text.trim()
                settings.dbName = dbNameTf.text.trim()

                toJSON(".settings", settings)

                stage.close()
            }
        }
    }

}

