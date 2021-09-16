package ldcapps.servicehelper

import javafx.animation.TranslateTransition
import javafx.scene.Node
import javafx.scene.control.TextField
import javafx.util.Duration

class Animations {
    companion object {
        fun emptyNode(node: Node) {
            val tt = TranslateTransition(Duration.millis(50.0), node)
            val keyTyped = node.onKeyTyped
            val style = "-fx-background-color: rgb(255,120,120); -fx-border-size: 1; -fx-border-color: gray;"

            node.style += style

            if (node is TextField)
                node.setOnKeyTyped {
                    node.style = node.style.replace(style, "")

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
    }
}