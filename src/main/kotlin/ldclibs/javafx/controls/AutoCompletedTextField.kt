package ldclibs.javafx.controls

import javafx.beans.InvalidationListener
import javafx.scene.Parent
import javafx.scene.control.*
import javafx.scene.layout.VBox
import ldcapps.servicehelper.toFXList

open class AutoCompletedTextField(
    var items: List<String>? = null,
    val textField: MyTextField? = null,
    private var filter: Regex? = null,
    private var replacement: (String, String) -> String = { c, _ -> c },
    var maxSize: Int? = null,
    var allCaps: Boolean = false
) : TextField() {
    private val popup = PopupControl()

    val node: Parent?
        get() =
            if (items != null)
                ListView(items!!.distinct().filter { it.contains(text, true) }.toFXList()).apply {
                    prefWidth = this@AutoCompletedTextField.width
                    prefHeight = items.size * 28.0 + 2
                    fixedCellSize = 28.0
                    maxHeight = 226.0

                    setOnMouseClicked {
                        selectionModel.selectedItems.getOrNull(0)?.let { item ->
                            popup.hide()

                            text = item
                            positionCaret(text.length)

                            onAutoCompleted(item)
                        }
                    }
                }
            else null

    var onAutoCompleted: (String) -> Unit = {}

    private fun showPopup() {
        if (node != null) {
            popup.isAutoHide = true
            popup.scene.root = node
            val p = localToScreen(0.0, height)
            popup.show(scene.window, p.x, p.y)
        }
    }


    init {
        setOnKeyTyped { showPopup() }
        focusedProperty().addListener(InvalidationListener { if (isFocused) showPopup() else popup.hide() })


        if (textField != null){
            replacement = textField.replacement
            maxSize = textField.maxSize
            filter = textField.filter
            allCaps = textField.allCaps
        }
    }

    override fun replaceText(start: Int, end: Int, text: String?) {
        val fText = if (allCaps) text!!.toUpperCase() else text!!
        if (((filter == null || fText.matches(filter!!)) && (maxSize == null || this.text.length <= maxSize!! - 1)) || fText == "")
            super.replaceText(start, end, replacement(if (allCaps) text.toUpperCase() else fText, this.text))
    }

    override fun replaceSelection(text: String?) {
        val fText = if (allCaps) text!!.toUpperCase() else text!!
        if (((filter == null || fText.matches(filter!!)) && (maxSize == null || this.text.length <= maxSize!! - 1)) || fText == "")
            super.replaceSelection(replacement(fText, this.text))
    }


}
