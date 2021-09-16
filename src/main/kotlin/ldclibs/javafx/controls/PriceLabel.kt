package ldclibs.javafx.controls

import javafx.scene.control.Label

class PriceLabel : Label() {
    var value = 0.0
        set(value) {
            text = String.format("%.2f", value).replace(",", ".")
            field = value
        }
        get() = text.toDouble()
}