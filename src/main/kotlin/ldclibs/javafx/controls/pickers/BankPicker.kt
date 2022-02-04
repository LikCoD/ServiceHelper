package ldclibs.javafx.controls.pickers

import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import ldcapps.servicehelper.Data
import ldcapps.servicehelper.NotNullField
import ldcapps.servicehelper.NotNullField.Companion.check
import ldcapps.servicehelper.data
import ldclibs.javafx.controls.AutoCompletedTextField
import ldclibs.javafx.controls.IntTextField
import ldclibs.javafx.controls.MyTextField
import ldclibs.javafx.controls.StringTextField

class BankPicker : CustomPicker() {
    var startBik: String = ""
    var pa: String = ""
        set(value) {
            this.value = value
            if (bank.isNotEmpty())
                this.value += " в $bank"
            this.value += " по адресу $bankAddress, БИК $bik"
            field = value
        }
    var bank: String = ""
        set(value) {
            this.value = pa
            if (value.isNotEmpty())
                this.value += " в $value"
            this.value += " по адресу $bankAddress, БИК $bik"
            field = value
        }
    var bankAddress: String = ""
        set(value) {
            this.value = pa
            if (bank.isNotEmpty())
                this.value += " в $bank"
            this.value += " по адресу $value, БИК $bik"
            field = value
        }
    var bik: String = ""
        set(value) {
            this.value = pa
            if (bank.isNotEmpty())
                this.value += " в $bank"
            this.value += " по адресу $bankAddress, БИК $value"
            field = value
        }

    @NotNullField(size = 2)
    private var t1 = StringTextField(2, true).apply {
        setOnKeyReleased {
            if (text.length == maxSize)
                t2.requestFocus()
        }

        text = "BY"
        prefHeight = 30.0
        prefWidth = 30.0
    }

    @NotNullField(size = 2)
    private var t2 = IntTextField(2).apply {
        setOnKeyReleased {
            if (text.length == maxSize)
                t3.requestFocus()
        }

        prefHeight = 30.0
        prefWidth = 30.0
    }

    @NotNullField(size = 4)
    private var t3 = StringTextField(4, true).apply {
        setOnKeyReleased {
            if (text.length == maxSize)
                t4.requestFocus()
        }

        prefHeight = 30.0
        prefWidth = 50.0
    }

    @NotNullField(size = 4)
    private var t4 = IntTextField(4).apply {
        setOnKeyReleased {
            if (text.length == maxSize)
                t5.requestFocus()
        }

        prefHeight = 30.0
        prefWidth = 45.0
    }

    @NotNullField(size = 4)
    private var t5 = IntTextField(4).apply {
        setOnKeyReleased {
            if (text.length == maxSize)
                t6.requestFocus()
        }

        prefHeight = 30.0
        prefWidth = 45.0
    }

    @NotNullField(size = 4)
    private var t6 = IntTextField(4).apply {
        setOnKeyReleased {
            if (text.length == maxSize)
                t7.requestFocus()
        }

        prefHeight = 30.0
        prefWidth = 45.0
    }

    @NotNullField(size = 4)
    private var t7 = IntTextField(4).apply {
        setOnKeyReleased {
            if (text.length == maxSize)
                t8.requestFocus()
        }

        prefHeight = 30.0
        prefWidth = 45.0
    }

    @NotNullField(size = 4)
    private var t8 = IntTextField(4).apply {
        setOnKeyReleased {
            if (text.length == maxSize)
                bankTf.requestFocus()
        }

        prefHeight = 30.0
        prefWidth = 45.0
    }

    @NotNullField
    private var bankTf = AutoCompletedTextField(data.banks.map { it.name }).apply {
        promptText = "Банк"
        items = data.banks.map { it.name }

        onAutoCompleted = { b ->
            val acBank = data.banks.find { it.name == b } ?: Data.Bank()
            bankAddressTf.text = acBank.address
            bikTf.text = acBank.bik
        }

        prefHeight = 30.0
        prefWidth = 405.0
    }

    @NotNullField
    private var bankAddressTf = MyTextField().apply {
        promptText = "Адрес банка"
        prefHeight = 30.0
        prefWidth = 295.0
    }

    @NotNullField
    private var bikTf = MyTextField(allCaps = true).apply {
        promptText = "БИК"
        prefHeight = 30.0
        prefWidth = 100.0
    }

    private var confirmBtn = Button("Подтвердить").apply {
        isDefaultButton = true
        setOnAction {
            if (!this@BankPicker.check()) return@setOnAction

            pa = "${t1.text}${t2.text} ${t3.text} ${t4.text} ${t5.text} ${t6.text} ${t7.text} ${t8.text}"
            startBik = t3.text
            bankAddress = bankAddressTf.text
            bik = bikTf.text
            bank = bankTf.text
            value = pa
            value += " в ${bankTf.text}"
            value += " по адресу $bankAddress, БИК $bik"

            onClose()
            hide()

        }
    }

    override var node: Parent = VBox(10.0).apply {
        if (bank.isNotEmpty())
            bankTf.text = bank
        style += "-fx-background-color: rgb(245,243,235); -fx-border-color: gray; -fx-border-radius: 3; -fx-padding: 5;"
        alignment = Pos.CENTER
        children += HBox(10.0).apply {
            alignment = Pos.CENTER
            children += t1
            children += t2
            children += t3
            children += t4
            children += t5
            children += t6
            children += t7
            children += t8
        }
        children += bankTf
        children += HBox(10.0).apply {
            children += bankAddressTf
            children += bikTf
        }
        children += confirmBtn
    }

    var onShow: () -> Unit = {}
    var onClose: () -> Unit = {}

    override fun onOpen() {
        if (pa.length >= 34) {
            t1.text = pa.substring(0..1)
            t2.text = pa.substring(2..3)
            t3.text = pa.substring(5..8)
            t4.text = pa.substring(10..13)
            t5.text = pa.substring(15..18)
            t6.text = pa.substring(20..23)
            t7.text = pa.substring(25..28)
            t8.text = pa.substring(30..33)
        }
        bankTf.text = bank
        bankAddressTf.text = bankAddress
        bikTf.text = bik
        t2.requestFocus()
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