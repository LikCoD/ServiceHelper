package ldclibs.javafx.controls.pickers

import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import ldcapps.servicehelper.NotNullField
import ldcapps.servicehelper.NotNullField.Companion.check
import ldclibs.javafx.controls.IntTextField

class PhonePicker : CustomPicker() {
    var phone: String = ""
        set(value) {
            this.value = "+$value"
            field = value
        }

    @NotNullField(size = 3)
    private var t1 = IntTextField(3).apply {
        setOnKeyReleased {
            if (text.length == maxSize)
                t2.requestFocus()
        }

        text = "375"
        prefHeight = 30.0
        prefWidth = 35.0
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

    @NotNullField(size = 3)
    private var t3 = IntTextField(3).apply {
        setOnKeyReleased {
            if (text.length == maxSize)
                t4.requestFocus()
        }

        prefHeight = 30.0
        prefWidth = 35.0
    }

    @NotNullField(size = 2)
    private var t4 = IntTextField(2).apply {
        setOnKeyReleased {
            if (text.length == maxSize)
                t5.requestFocus()
        }

        prefHeight = 30.0
        prefWidth = 30.0
    }

    @NotNullField(size = 2)
    private var t5 = IntTextField(2).apply {
        prefHeight = 30.0
        prefWidth = 30.0
    }

    private var confirmBtn = Button("Подтвердить").apply {
        isDefaultButton = true

        setOnAction {
            if (this@PhonePicker.check()) {
                phone = "${t1.text} ${t2.text} ${t3.text} ${t4.text} ${t5.text}"
                value = "+$phone"
                onClose()
                hide()
            }
        }
    }

    override var node: Parent = VBox(10.0).apply {
        value = "+$phone"
        style += "-fx-background-color: rgb(245,243,235); -fx-border-color: gray; -fx-border-radius: 3; -fx-padding: 5;"
        alignment = Pos.CENTER
        children += HBox(10.0).apply {
            alignment = Pos.CENTER
            children += Text("+")
            children += t1
            children += t2
            children += t3
            children += t4
            children += t5
        }
        children += confirmBtn
    }

    var onShow: () -> Unit = {}
    var onClose: () -> Unit = {}

    override fun onOpen() {
        t2.requestFocus()
        if (phone.length >= 16) {
            t1.text = phone.substring(0..2)
            t2.text = phone.substring(4..5)
            t3.text = phone.substring(7..9)
            t4.text = phone.substring(11..12)
            t5.text = phone.substring(14..15)
        }
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