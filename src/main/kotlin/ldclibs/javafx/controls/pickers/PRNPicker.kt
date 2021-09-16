package ldclibs.javafx.controls.pickers

import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import ldcapps.servicehelper.inSize
import ldcapps.servicehelper.isNotNull
import ldcapps.servicehelper.isOnline
import ldclibs.javafx.controls.IntTextField
import ldclibs.javafx.controls.MyTextField
import ldclibs.javafx.controls.StringTextField
import org.jsoup.Jsoup

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
    private var prnTf = IntTextField(9).apply {
        setOnKeyReleased {
            if (text.length == maxSize)
                companyTf.requestFocus()
        }

        promptText = "УНП"
        setOnAction {
            if (isOnline) {
                val prop =
                    Jsoup.connect("https://kartoteka.by/unp-$text").get().body().getElementsByAttribute("itemprop")
                companyTf.text = prop.find { it.attr("itemprop") == "alternateName" }?.text() ?: "-"
                addressTf.text = prop.find { it.attr("itemprop") == "addressLocality" }?.text() ?: "-"
            }
        }
        prefHeight = 30.0
        prefWidth = 100.0
    }
    private var companyTf = MyTextField().apply {
        promptText = "Компания"
        prefHeight = 30.0
        prefWidth = 140.0
    }
    private var addressTf = MyTextField().apply {
        promptText = "Адрес"
        prefHeight = 30.0
        prefWidth = 150.0
    }

    private var confirmBtn = Button("Подтвердить").apply {
        isDefaultButton = true

        setOnAction {
            if (isNotNull(companyTf, addressTf) && inSize(prnTf to 9)) {
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