package ldclibs.javafx.controls

import javafx.scene.control.TableCell
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode

open class MyTableCell<S>(var textField: TextField = TextField()) : TableCell<S, String>() {

    override fun startEdit() {
        super.startEdit()
        textField.text = item

        textField.setOnAction {
            commitEdit(textField.text)
            it.consume()
        }

        textField.setOnKeyReleased {
            if (it.code == KeyCode.ESCAPE) {
                cancelEdit()
                it.consume()
            }
        }

        text = null
        graphic = textField
    }

    override fun cancelEdit() {
        super.cancelEdit()
        text = item
        graphic = null
    }

    public override fun updateItem(item: String?, bool: Boolean) {
        super.updateItem(item, bool)
        when {
            isEmpty -> {
                text = null
                graphic = null
            }
            isEditing -> {
                textField.text = this.item
                text = null
                graphic = textField
            }
            else -> {
                text = item
                graphic = null
            }
        }
    }

    fun setFactory() {

    }
}