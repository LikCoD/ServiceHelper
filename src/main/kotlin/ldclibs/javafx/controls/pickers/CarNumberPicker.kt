package ldclibs.javafx.controls.pickers

import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.TextField
import ldcapps.servicehelper.NotNullField.Companion.check
import ldcapps.servicehelper.fxList
import ldclibs.javafx.controls.intTextfield
import ldclibs.javafx.controls.stringTextfield
import tornadofx.*
import kotlin.properties.Delegates

class CarNumberPicker(var number: Int = 0, var letters: String = "", var region: Int = 0) : CustomPicker() {

    override var node: Parent = vbox(spacing = 10) {
        style += "-fx-background-color: rgb(245,243,235); -fx-border-color: gray; -fx-border-radius: 3; -fx-padding: 5;"
        alignment = Pos.CENTER

        val patterns = fxList("", "")

        var lettersTf by Delegates.notNull<TextField>()
        var numbersTf by Delegates.notNull<TextField>()
        var regionTf by Delegates.notNull<TextField>()

        hbox(spacing = 10) {
            alignment = Pos.CENTER

            var number = ""
            var letter = ""
            var region = ""

            fun updatePatterns() {
                patterns[0] = "$number $letter-$region"
                patterns[1] = "$letter $number-$region"
            }

            numbersTf = intTextfield(maxSize = 4) {
                setOnKeyReleased {
                    number = text
                    updatePatterns()
                    if (text.length == maxSize) lettersTf.requestFocus()
                }

                prefHeight = 30.0
                prefWidth = 45.0
            }

            lettersTf = stringTextfield(maxSize = 2, allCaps = true) {
                setOnKeyReleased {
                    letter = text
                    updatePatterns()
                    if (text.length == maxSize) regionTf.requestFocus()
                }

                prefHeight = 30.0
                prefWidth = 35.0
            }

            text("-")

            regionTf = intTextfield(maxSize = 1) {
                setOnKeyReleased {
                    region = text
                    updatePatterns()
                }

                prefHeight = 30.0
                prefWidth = 30.0
            }
        }

        val patternCb = combobox<String> {
            maxWidth = Double.MAX_VALUE
            items = patterns
            value = patterns[0]
        }

        button("Подтвердить") {
            isDefaultButton = true

            setOnAction {
                if (this@CarNumberPicker.check()) {
                    letters = lettersTf.text
                    number = numbersTf.text.toInt()
                    region = regionTf.text.toInt()

                    value = if (patternCb.value != "") patternCb.value else patterns[0]

                    onClose()
                    this@CarNumberPicker.hide()
                }
            }
        }
    }

    var onShow: () -> Unit = {}
    var onClose: () -> Unit = {}

    override fun onOpen() = onShow()

    init {
        editor.isEditable = iseditable
        isEditable = true
    }
}