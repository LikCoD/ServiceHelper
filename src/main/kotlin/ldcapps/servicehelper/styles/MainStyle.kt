package ldcapps.servicehelper.styles

import javafx.scene.paint.Color
import tornadofx.*

class MainStyle : Stylesheet() {
    companion object {
        val mainWindow by cssclass()

        val toolButton by cssclass()
        val confirmButton by cssclass()

        val windowBackgroundColor = c("#ffc876")

        val buttonBackgroundColor = c("#162b3f")
        val buttonHoverBackgroundColor = c("#162b3f", .5)

        val confirmButtonTextColor = c("#ebf3f5")
        val confirmButtonBackground = c("#1a776f")
        val confirmButtonHoverBackgroundColor = c("#1a776f", .5)
        val confirmButtonPressedBackgroundColor = c("#1a776f", .25)
    }

    init {
        mainWindow {
            backgroundColor += windowBackgroundColor

            datePicker {
                prefHeight = 30.px
            }
            textField {
                prefHeight = 30.px
            }
            comboBox {
                prefHeight = 30.px
            }
        }


        toolButton {
            backgroundColor += buttonBackgroundColor
            backgroundRadius += box(0.px)

            and(hover) { backgroundColor += buttonHoverBackgroundColor }
            and(pressed) { backgroundColor += Color.TRANSPARENT }
        }

        confirmButton {
            fontSize = 15.px
            textFill = confirmButtonTextColor
            backgroundColor += confirmButtonBackground

            prefHeight = 35.px

            and(hover) { backgroundColor += confirmButtonHoverBackgroundColor }
            and(pressed) { backgroundColor += confirmButtonPressedBackgroundColor }
        }
    }
}