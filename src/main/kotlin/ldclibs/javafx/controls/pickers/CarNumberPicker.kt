package ldclibs.javafx.controls.pickers

import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import ldcapps.servicehelper.inSize
import ldclibs.javafx.controls.IntTextField
import ldclibs.javafx.controls.StringTextField

class CarNumberPicker(var number: Int = 0, var letters: String = "", var region: Int = 0) : CustomPicker() {

    private var numberTf = IntTextField(4).apply {
        setOnKeyReleased {
            if (text.length == maxSize)
                lettersTf.requestFocus()
        }

        prefHeight = 30.0
        prefWidth = 45.0
    }
    private var lettersTf = StringTextField(2, true).apply {
        setOnKeyReleased {
            if (text.length == maxSize)
                regionTf.requestFocus()
        }

        prefHeight = 30.0
        prefWidth = 35.0
    }
    private var regionTf = IntTextField(1).apply {
        prefHeight = 30.0
        prefWidth = 25.0
    }

    private var isBusCb = CheckBox()

    private var confirmBtn = Button("Подтвердить").apply {
        isDefaultButton = true

        setOnAction {
            if (inSize(numberTf to 4, lettersTf to 2, regionTf to 1)) {
                letters = lettersTf.text.toUpperCase()
                number = numberTf.text.toInt()
                region = regionTf.text.toInt()
                value = if (isBusCb.isSelected)
                    "$letters $number-$region"
                else
                    "$number $letters-$region"
                onClose()
                hide()
            }
        }
    }

    override var node: Parent = VBox(10.0).apply {
        style += "-fx-background-color: rgb(245,243,235); -fx-border-color: gray; -fx-border-radius: 3; -fx-padding: 5;"
        alignment = Pos.CENTER
        children += HBox(10.0).apply{
            alignment = Pos.CENTER
            children += numberTf
            children += lettersTf
            children += Text("-")
            children += regionTf
            children += isBusCb
        }
        children += confirmBtn
    }

    var onShow: () -> Unit = {}
    var onClose: () -> Unit = {}

    override fun onOpen() {
        onShow()
    }

    init {
        editor.isEditable = iseditable
        isEditable = true
    }
}