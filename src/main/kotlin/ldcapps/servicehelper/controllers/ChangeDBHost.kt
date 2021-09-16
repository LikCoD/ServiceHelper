package ldcapps.servicehelper.controllers

import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.stage.Stage
import ldcapps.servicehelper.inSize
import ldcapps.servicehelper.isNotNull
import ldcapps.servicehelper.settings
import ldcapps.servicehelper.toJSON
import ldclibs.javafx.controls.IntTextField
import ldclibs.javafx.controls.MyTextField
import java.net.URL
import java.util.*

class ChangeDBHost : Initializable {
    lateinit var stage: Stage
    lateinit var hostTf: MyTextField
    lateinit var portTf: IntTextField
    lateinit var loginTf: MyTextField
    lateinit var passwordTf: MyTextField
    lateinit var dbNameTf: MyTextField
    lateinit var confirmBtn: Button

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        hostTf.text = settings.host
        portTf.text = settings.port.toString()
        loginTf.text = settings.login
        passwordTf.text = settings.password
        dbNameTf.text = settings.dbName

        confirmBtn.setOnAction {
            if (isNotNull(hostTf, portTf, loginTf, passwordTf, dbNameTf) && inSize(portTf to 4)){
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

