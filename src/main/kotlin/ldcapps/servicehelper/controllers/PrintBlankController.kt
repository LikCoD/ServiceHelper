package ldcapps.servicehelper.controllers

import javafx.fxml.Initializable
import javafx.print.PageOrientation
import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import ldcapps.servicehelper.Dialogs
import java.awt.Toolkit
import java.net.URL
import java.util.*

class PrintBlankController : Initializable {
    lateinit var stage: Stage
    lateinit var mainAp: AnchorPane
    lateinit var frontAp: AnchorPane
    lateinit var backAp: AnchorPane
    lateinit var confirmBtn: Button
    lateinit var printBtn: Button

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        if (Toolkit.getDefaultToolkit().screenSize.height.toDouble() < mainAp.prefHeight - 50)
            stage.height = (Toolkit.getDefaultToolkit().screenSize.height - 100).toDouble()

        stage.setOnShown { Dialogs.print(stage, PageOrientation.LANDSCAPE, frontAp, backAp) }
        printBtn.setOnAction { Dialogs.print(stage, PageOrientation.LANDSCAPE, frontAp, backAp) }
    }
}

