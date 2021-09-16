package ldclibs.javafx.controls

import javafx.scene.control.TextField

open class MyTextField(
    val filter: Regex? = null,
    val replacement: (String, String) -> String = { c, _ -> c },
    var maxSize: Int? = null,
    var allCaps: Boolean = false
) : TextField() {

    override fun replaceText(start: Int, end: Int, text: String?) {
        val fText = if (allCaps) text!!.toUpperCase() else text!!
        if (((filter == null || fText.matches(filter)) && (maxSize == null || this.text.length <= maxSize!! - 1)) || fText == "")
            super.replaceText(start, end, replacement(if (allCaps) text.toUpperCase() else fText, this.text))
    }

    override fun replaceSelection(text: String?) {
        val fText = if (allCaps) text!!.toUpperCase() else text!!
        if (((filter == null || fText.matches(filter)) && (maxSize == null || this.text.length <= maxSize!! - 1)) || fText == "")
            super.replaceSelection(replacement(fText, this.text))
    }
}