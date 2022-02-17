package ldclibs.javafx.controls

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TextField
import javafx.scene.control.cell.ComboBoxTableCell
import javafx.util.Callback
import javafx.util.converter.DefaultStringConverter
import java.text.DecimalFormat
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties

class Column<S : Any> : TableColumn<S, String>() {
    var propName: String? = null

    fun setValueFactory(name: String) {
        propName = name
        setValueFactory { s ->
            s::class.declaredMemberProperties.find { it.name == propName }?.getter?.call(s)
        }
    }

    fun numCol() {
        cellFactory = Callback<TableColumn<S, String?>?, TableCell<S, String?>?> {
            object : TableCell<S, String?>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (tableRow != null)
                        text = (tableRow.index + 1).toString()
                }
            }
        }
    }

    inline fun setValueFactory(crossinline value: (S) -> Any?) =
        setCellValueFactory { SimpleStringProperty(value(it.value).toString()) }

    fun setPriceValueFactory(name: String) {
        propName = name

        setValueFactory {s ->
            val price = s::class.declaredMemberProperties.find { it.name == propName }?.getter?.call(s)
            DecimalFormat("#0.00").format(price)
        }
    }

    inline fun setPriceValueFactory(name: String? = null, crossinline value: (S) -> Number?) {
        if (name != null) propName = name

        setCellValueFactory {
            val res = value(it.value)?.toDouble() ?: return@setCellValueFactory SimpleStringProperty("-")

            val price = DecimalFormat("#0.00").format(res)
            SimpleStringProperty(price)
        }
    }

    fun setPriceTextFieldCellFactory() =
        setCellFactory { object : PriceTextFieldTableCell<S>() {} }

    fun setTextFieldCellFactory(textField: TextField = TextField()) =
        setCellFactory { object : MyTableCell<S>(textField) {} }

    fun setDoubleTextFieldCellFactory() =
        setCellFactory { object : DoubleTextFieldTableCell<S>() {} }

    fun setIntTextFieldCellFactory() =
        setCellFactory { object : IntTextFieldTableCell<S>() {} }

    fun setComboBoxCellFactory(vararg el: String) {
        cellFactory = ComboBoxTableCell.forTableColumn(DefaultStringConverter(), FXCollections.observableArrayList(*el))
    }

    var onEditCommitted: EventHandler<CellEditEvent<S, String>>? = null

    val onEditCommit: Nothing? = null

    init {
        if (isEditable)
            setOnEditCommit { event ->
                if (propName != null) {
                    val item = tableView.selectionModel.selectedItem!!
                    val field = item::class.declaredMemberProperties.find { it.name == propName }

                    if (field !is KMutableProperty<*>) return@setOnEditCommit

                    when (field.getter.call(item)) {
                        is String -> field.setter.call(item, event.newValue)
                        is Int -> field.setter.call(item, event.newValue.toIntOrNull() ?: 0)
                        is Double -> field.setter.call(item, event.newValue.toDoubleOrNull() ?: 0.0)
                    }

                    tableView.refresh()
                }

                if (onEditCommitted != null) {
                    onEditCommitted!!.handle(event)

                    tableView.refresh()
                }
            }
    }
}