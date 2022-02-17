package ldclibs.javafx.controls

import javafx.beans.property.ObjectProperty
import javafx.event.EventTarget
import org.intellij.lang.annotations.Language
import tornadofx.attachTo

open class IntTextField(maxSize: Int? = null, allCaps: Boolean = false, valueProperty: ObjectProperty<String>? = null) :
    MyTextField(
        filter = "\\d",
        maxSize = maxSize,
        allCaps = allCaps,
        valueProperty = valueProperty
    )

class DoubleTextField(maxSize: Int? = null, allCaps: Boolean = false) :
    MyTextField(
        filter = "[\\d,.]",
        replacement = { c, s ->
            val res = if (c == ",") "." else c

            if ((s.contains('.') || s.isEmpty()) && res == ".") ""
            else res
        },
        maxSize = maxSize,
        allCaps = allCaps
    )

class StringTextField(maxSize: Int? = null, allCaps: Boolean = false) :
    MyTextField(
        filter = "[a-z A-Z,.а-я\"А-я]",
        maxSize = maxSize,
        allCaps = allCaps
    )

class PriceTextField(maxSize: Int? = null, allCaps: Boolean = false) :
    MyTextField(
        filter = "[\\d,.]",
        replacement = { c, s ->
            val res = if (c == ",") "." else c

            if ((s.contains('.') || s.isEmpty()) && (res == "." || s.substringAfter(".").length > 1)) ""
            else res
        },
        maxSize = maxSize,
        allCaps = allCaps
    )


class AutoCompletedIntTextField(maxSize: Int? = null, allCaps: Boolean = false) :
    AutoCompletedTextField<Int>(textField = IntTextField(maxSize, allCaps))

class AutoCompletedDoubleTextField(maxSize: Int? = null, allCaps: Boolean = false) :
    AutoCompletedTextField<Double>(textField = DoubleTextField(maxSize, allCaps))

class AutoCompletedStringTextField(maxSize: Int? = null, allCaps: Boolean = false) :
    AutoCompletedTextField<String>(textField = StringTextField(maxSize, allCaps))

class AutoCompletedPriceTextField(maxSize: Int? = null, allCaps: Boolean = false) :
    AutoCompletedTextField<Double>(textField = PriceTextField(maxSize, allCaps))


open class IntTextFieldTableCell<S>(maxSize: Int? = null, allCaps: Boolean = false) :
    MyTableCell<S>(PriceTextField(maxSize, allCaps))

open class DoubleTextFieldTableCell<S>(maxSize: Int? = null, allCaps: Boolean = false) :
    MyTableCell<S>(DoubleTextField(maxSize, allCaps))

open class StringTextFieldTableCell<S>(maxSize: Int? = null, allCaps: Boolean = false) :
    MyTableCell<S>(StringTextField(maxSize, allCaps))

open class PriceTextFieldTableCell<S>(maxSize: Int? = null, allCaps: Boolean = false) :
    MyTableCell<S>(PriceTextField(maxSize, allCaps))

fun EventTarget.intTextfield(
    value: String? = null,
    maxSize: Int? = null,
    op: IntTextField.() -> Unit = {}
) = IntTextField(maxSize, false).attachTo(this, op) {
    if (value != null) it.text = value
}

fun EventTarget.intTextfield(
    valueProperty: ObjectProperty<String>,
    maxSize: Int? = null,
    op: IntTextField.() -> Unit = {}
) = IntTextField(maxSize, false, valueProperty).attachTo(this, op)

fun EventTarget.stringTextfield(
    value: String? = null,
    maxSize: Int? = null,
    allCaps: Boolean = false,
    op: StringTextField.() -> Unit = {}
) = StringTextField(maxSize, allCaps).attachTo(this, op) {
    if (value != null) it.text = value
}

fun <T> EventTarget.autocompletedTextfield(
    valueProperty: ObjectProperty<T>? = null,
    items: List<T>? = null,
    @Language("RegExp") filter: String? = null,
    maxSize: Int? = null,
    allCaps: Boolean = false,
    getString: (T) -> String = { it.toString() },
    replacement: (String, String) -> String = { c, _ -> c },
    op: AutoCompletedTextField<*>.() -> Unit = {}
) = AutoCompletedTextField(
    items,
    null,
    if (filter != null) Regex(filter) else null,
    replacement,
    maxSize,
    allCaps,
    getString,
    valueProperty
).attachTo(this, op)

fun EventTarget.myTextfield(
    @Language("RegExp") filter: String? = null,
    replacement: (String, String) -> String = { c, _ -> c },
    maxSize: Int? = null,
    allCaps: Boolean = false,
    valueProperty: ObjectProperty<String>? = null,
    op: MyTextField.() -> Unit = {}
) = MyTextField(filter, replacement, maxSize, allCaps, valueProperty).attachTo(this, op)