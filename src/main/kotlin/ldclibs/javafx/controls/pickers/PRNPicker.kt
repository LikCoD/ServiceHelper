package ldclibs.javafx.controls.pickers

import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ldcapps.servicehelper.Animations
import ldcapps.servicehelper.NotNullField
import ldcapps.servicehelper.NotNullField.Companion.check
import ldclibs.javafx.controls.IntTextField
import ldclibs.javafx.controls.MyTextField
import java.net.URL

class PRNPicker : CustomPicker() {
    var prn: Int? = 0
        set(value) {
            this.value = "$company, Адрес $address, УНП $value"
            field = value
        }
    var company: String = ""
        set(value) {
            this.value = "$value, Адрес $address, УНП $prn"
            field = value
        }
    var address: String = ""
        set(value) {
            this.value = "$company, Адрес $value, УНП $prn"
            field = value
        }

    @NotNullField(size = 9)
    private var prnTf = IntTextField(9).apply {
        setOnKeyReleased {
            if (text.length == maxSize) {
                companyTf.requestFocus()
                try {
                    val url = URL("https://www.portal.nalog.gov.by/grp/getData?unp=$text&charset=UTF-8&type=json")
                    val json = Json.parseToJsonElement(url.openStream().reader().readText()).jsonObject["ROW"]?.jsonObject
                        ?: return@setOnKeyReleased

                    companyTf.text = json["VNAIMK"]?.jsonPrimitive?.contentOrNull ?: "-"
                    addressTf.text = json["VPADRES"]?.jsonPrimitive?.contentOrNull ?: "-"
                } catch (_: Exception) {
                    Animations.warningNode(this)
                }
            }
        }

        promptText = "УНП"
        prefHeight = 30.0
        prefWidth = 100.0
    }

    @NotNullField
    private var companyTf = MyTextField().apply {
        promptText = "Компания"
        prefHeight = 30.0
        prefWidth = 140.0
    }

    @NotNullField
    private var addressTf = MyTextField().apply {
        promptText = "Адрес"
        prefHeight = 30.0
        prefWidth = 150.0
    }

    private var confirmBtn = Button("Подтвердить").apply {
        isDefaultButton = true

        setOnAction {
            if (this@PRNPicker.check()) {
                prn = prnTf.text.toInt()
                company = companyTf.text
                address = addressTf.text
                value = "$company, Адрес $address, УНП $prn"
                onClose()
                hide()
            }
        }
    }

    override var node: Parent = VBox(10.0).apply {
        style += "-fx-background-color: rgb(245,243,235); -fx-border-color: gray; -fx-border-radius: 3; -fx-padding: 5;"
        alignment = Pos.CENTER
        children += HBox(10.0).apply {
            alignment = Pos.CENTER
            children += prnTf
            children += companyTf
        }
        children += addressTf
        children += confirmBtn
    }

    var onShow: () -> Unit = {}
    var onClose: () -> Unit = {}

    override fun onOpen() {
        prnTf.text = prn.toString()
        companyTf.text = company
        addressTf.text = address
        if (prn != 0)
            value = "$company, Адрес $address, УНП $prn"
        onShow()
    }

    override fun onHide() {
        onClose()
    }

    init {
        editor.isEditable = iseditable
        isEditable = true
    }
}