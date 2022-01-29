package ldcapps.servicehelper

import javafx.animation.TranslateTransition
import javafx.application.Platform
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.util.Duration
import kotlin.concurrent.thread

object Animations {

    enum class Style(val value: String){
        EMPTY("-fx-background-color: rgb(255,75,75);"),
        WARNING("-fx-background-color: rgb(225,170,0);"),
        ERROR("-fx-background-color: rgb(255,75,75);"),
    }

    fun emptyNode(node: Node, style: Style = Style.EMPTY){
        val tt = TranslateTransition(Duration.millis(50.0), node)
        val keyTyped = node.onKeyTyped

        node.style += style.value

        if (node is TextField)
            node.setOnKeyTyped {
                node.style = node.style.replace(style.value, "")

                if (keyTyped != null) {
                    keyTyped.handle(it)
                    node.onKeyTyped = keyTyped
                }

            }

        tt.fromX = 0.0
        tt.toX = 5.0
        tt.cycleCount = 6
        tt.isAutoReverse = true
        tt.play()
    }

    fun warningNode(node: Node) = emptyNode(node, Style.WARNING)

    fun errorButton(button: Button, text: String, style: Style = Style.ERROR, duration: Long = 1000) {
        if (button.style.contains(style.value)) return

        val usualText = button.text

        emptyNode(button, style)
        button.text = text

        thread {
            Thread.sleep(duration)
            Platform.runLater{
                button.style = button.style.replace(style.value, "")
                button.text = usualText
            }
        }
    }
}