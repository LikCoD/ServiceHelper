package ldclibs.javafx.controls.pickers

import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.TextField
import ldcapps.servicehelper.NotNullField
import ldcapps.servicehelper.NotNullField.Companion.check
import ldclibs.javafx.controls.AutoCompletedTextField
import ldclibs.javafx.controls.autocompletedTextfield
import ldclibs.javafx.controls.myTextfield
import tornadofx.*
import kotlin.properties.Delegates

class VINPicker(vinInfoList: List<VinInfo> = emptyList()) : CustomPicker() {

    var year: Int = 0
    var vinInfoList = vinInfoList
        set(value) {
            field = value

            infoTf.items = value
        }

    var selectedInfo: VinInfo? = null

    @NotNullField(size = 3)
    var worldIndexTf by Delegates.notNull<TextField>()

    @NotNullField(size = 6)
    var infoTf by Delegates.notNull<AutoCompletedTextField<VinInfo>>()

    @NotNullField(size = 8)
    var uniqueTf by Delegates.notNull<TextField>()

    override var node: Parent = vbox(spacing = 10) {
        style += "-fx-background-color: rgb(245,243,235); -fx-border-color: gray; -fx-border-radius: 3; -fx-padding: 5;"
        alignment = Pos.CENTER

        hbox(spacing = 10) {
            worldIndexTf = myTextfield(filter = "\\w", allCaps = true, maxSize = 3) {
                setOnKeyReleased {
                    if (text.length == maxSize) infoTf.requestFocus()
                }

                prefWidth = 45.0
                prefHeight = 30.0
            }

            infoTf = autocompletedTextfield(
                items = vinInfoList,
                filter = "\\w",
                allCaps = true,
                maxSize = 6,
                getString = { it.info }) {
                setOnKeyReleased {
                    if (text.length == maxSize) uniqueTf.requestFocus()
                }

                onAutoCompleted = {
                    selectedInfo = it as VinInfo

                    uniqueTf.requestFocus()
                }

                prefWidth = 70.0
                prefHeight = 30.0
            }

            uniqueTf = myTextfield(filter = "\\w", allCaps = true, maxSize = 8) {
                prefWidth = 90.0
                prefHeight = 30.0
            }
        }

        button("Подтвердить") {
            isDefaultButton = true

            action {
                if (!this@VINPicker.check()) return@action

                value = "${worldIndexTf.text}${infoTf.text}${uniqueTf.text}"

                year = when (val code = value[9].code) {
                    in 49..57 -> code - 48
                    in 65..72 -> code - 55
                    in 74..78 -> code - 56
                    80 -> 23
                    in 82..85 -> code - 58
                    in 87..90 -> code - 59
                    else -> 0
                } + 2000

                onClose()
                this@VINPicker.hide()
            }
        }
    }

    var onClose: () -> Unit = {}

    init {
        editor.isEditable = iseditable
        isEditable = true
    }

    data class VinInfo(val info: String, val engine: Double, val model: String)
}