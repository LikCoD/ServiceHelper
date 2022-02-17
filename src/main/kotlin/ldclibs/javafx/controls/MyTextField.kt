package ldclibs.javafx.controls

import javafx.beans.property.ObjectProperty
import javafx.scene.control.TextField
import org.intellij.lang.annotations.Language

open class MyTextField(
    @Language("RegExp") filter: String? = null,
    val replacement: (String, String) -> String = { c, _ -> c },
    var maxSize: Int? = null,
    var allCaps: Boolean = false,
    private val valueProperty: ObjectProperty<String>? = null
) : TextField() {

    val filter: Regex? = if (filter != null) Regex(filter) else null

    override fun replaceText(start: Int, end: Int, text: String?) {
        val fText = if (allCaps) text!!.uppercase() else text!!
        if (((filter == null || fText.matches(filter)) && (maxSize == null || this.text.length <= maxSize!! - 1)) || fText == "")
            super.replaceText(start, end, replacement(if (allCaps) text.uppercase() else fText, text))

        valueProperty?.set(this.text)
    }

    override fun replaceSelection(text: String?) {
        val fText = if (allCaps) text!!.uppercase() else text!!
        if (((filter == null || fText.matches(filter)) && (maxSize == null || this.text.length <= maxSize!! - 1)) || fText == "")
            super.replaceSelection(replacement(fText, text))

        valueProperty?.set(this.text)
    }
}