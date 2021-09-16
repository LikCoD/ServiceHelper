package ldclibs.javafx.controls.pickers

import javafx.scene.Parent
import javafx.scene.control.ComboBox
import javafx.scene.control.PopupControl
import javafx.scene.layout.Pane

abstract class CustomPicker : ComboBox<String>() {

    var iseditable = false
    private val popup = PopupControl()
    open var node: Parent = Pane()

    open fun onOpen(){}
    open fun onHide(){}

    override fun hide() {
        popup.hide()
        super.hide()
    }

    override fun usesMirroring(): Boolean {
        setOnShowing {
            popup.isAutoHide = true
            popup.scene.root = node
            val p = localToScreen(0.0, height)
            popup.show(scene.window, p.x, p.y)
            onOpen()
        }
        setOnHiding {
            onHide()
        }
        return super.usesMirroring()
    }

    init{
        stylesheets += this::class.java.classLoader.getResource("css/emptyComboBox.css")?.toExternalForm()
        isEditable = true
        editor.isEditable = iseditable
    }
}
