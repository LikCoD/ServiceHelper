package ldclibs.javafx.controls

open class IntTextField(maxSize: Int? = null, allCaps: Boolean = false) :
    MyTextField(
        filter = Regex("[0-9]"),
        maxSize = maxSize,
        allCaps = allCaps
    )

class DoubleTextField(maxSize: Int? = null, allCaps: Boolean = false) :
    MyTextField(
        filter = Regex("[0-9,.]"),
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
        filter = Regex("[a-z A-Z,.а-я\"А-я]"),
        maxSize = maxSize,
        allCaps = allCaps
    )

class PriceTextField(maxSize: Int? = null, allCaps: Boolean = false) :
    MyTextField(
        filter = Regex("[0-9,.]"),
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