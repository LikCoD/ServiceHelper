package ldcapps.servicehelper.controllers

import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.RadioButton
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import ldcapps.servicehelper.*
import ldcapps.servicehelper.controllers.tools.Signup
import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.db.DataClasses.Companion.user
import java.io.File
import java.net.URL
import java.util.*


class Login : Initializable {
    lateinit var stage: Stage
    lateinit var signupBtn: Button
    lateinit var oosLocateTf: TextField
    lateinit var oosLocateBtn: Button
    lateinit var actsLocateTf: TextField
    lateinit var actsLocateBtn: Button
    lateinit var contractsLocateTf: TextField
    lateinit var contractsLocateBtn: Button
    lateinit var confirmBtn: Button
    lateinit var stayOfflineBtn: RadioButton
    lateinit var loginVb: VBox
    lateinit var signupPane: AnchorPane
    lateinit var pane: AnchorPane
    lateinit var loginTf: TextField
    lateinit var passwordTf: TextField

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        oosLocateBtn.setOnAction { oosLocateTf.text = Dialogs.getDirectory(Signup.stage, oosLocateTf.text) }
        actsLocateBtn.setOnAction { actsLocateTf.text = Dialogs.getDirectory(Signup.stage, actsLocateTf.text) }
        contractsLocateBtn.setOnAction {
            contractsLocateTf.text = Dialogs.getDirectory(Signup.stage, contractsLocateTf.text)
        }

        Signup.type = Signup.Companion.Type.SING_UP
        Signup.stage = stage

        signupPane = FXMLLoader(javaClass.classLoader.getResource("fxml/Tools/Signup.fxml")).load()
        (signupPane.children[0] as VBox).children.add(0, HBox().apply {
            alignment = Pos.CENTER_RIGHT
            spacing = 10.0

            children.add(Button("Вход").apply {
                prefWidth = 70.0
                id = "login"
                stylesheets += javaClass.classLoader.getResource("css/toolsBtns.css")?.toURI().toString()
                setOnAction {
                    pane.children.clear()
                    pane.children.add(loginVb)
                }
            })
        })

        signupBtn.setOnAction {
            pane.children.clear()
            pane.children.add(signupPane)
        }

        confirmBtn.setOnAction {
            if (isNotNull(loginTf, passwordTf, oosLocateTf, actsLocateTf, contractsLocateTf)) {
                File("${oosLocateTf.text}/Нал").mkdirs()
                File("${oosLocateTf.text}/Безнал").mkdirs()
                File(actsLocateTf.text).mkdirs()
                File(contractsLocateTf.text).mkdirs()

                user = DataClasses.db?.getUserUsingLogin(loginTf.text, passwordTf.text, DataClasses.User()) ?: DataClasses.User()

                if (user != DataClasses.User()) {
                    if (stayOfflineBtn.isSelected)
                        toJSON(".user", user)

                    settings = Settings(oosLocateTf.text, contractsLocateTf.text, actsLocateTf.text)
                    toJSON(".settings", settings)

                    toJSON(".report", DataClasses.reports)
                    toJSON(".cars", DataClasses.cars)
                    toJSON(".companies", DataClasses.companies)
                    toJSON(".owners", DataClasses.owners)
                    toJSON(".individuals", DataClasses.individuals)

                    Windows.ooController
                    stage.close()
                } else
                    Dialogs.warning("Логин и/или пароль введены неправильно")
            }
        }
    }
}

