package ldclibs.javafx.controls

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.ComboBoxTableCell
import javafx.util.Callback
import javafx.util.converter.DefaultStringConverter
import java.text.DecimalFormat

class Column<S> : TableColumn<S, String>() {
    var propName: String? = null

    fun setValueFactory(name: String) {
        propName = name
        setValueFactory {
            val field = it!!::class.java.getDeclaredField(propName!!)
            field.isAccessible = true
            field.get(it).toString()
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

    inline fun setValueFactory(crossinline value: (S) -> Any) =
        setCellValueFactory { SimpleStringProperty(value(it.value).toString()) }

    fun setPriceValueFactory(name: String) {
        propName = name

        setValueFactory {
            val field = it!!::class.java.getDeclaredField(propName!!)
            field.isAccessible = true
            val res = field.getDouble(it)
            DecimalFormat("#0.00").format(res)
        }
    }

    inline fun setPriceValueFactory(name: String? = null, crossinline value: (S) -> Number?) {
        if (name != null) propName = name

        setCellValueFactory {
            val res = value(it.value)?.toDouble()?: return@setCellValueFactory SimpleStringProperty("-")

            val price = DecimalFormat("#0.00").format(res)
            SimpleStringProperty(price)
        }
    }

    fun setPriceTextFieldCellFactory() =
        setCellFactory { object : PriceTextFieldTableCell<S>() {} }

    fun setTextFieldCellFactory() =
        setCellFactory { object : MyTableCell<S>() {} }

    fun setDoubleTextFieldCellFactory() =
        setCellFactory { object : DoubleTextFieldTableCell<S>() {} }

    fun setIntTextFieldCellFactory() =
        setCellFactory { object : IntTextFieldTableCell<S>() {} }

    fun setComboBoxCellFactory(vararg el: String) {
        cellFactory = ComboBoxTableCell.forTableColumn(DefaultStringConverter(), FXCollections.observableArrayList(*el))
    }

    var onEditCommitted: EventHandler<CellEditEvent<S, String>>? = null

    var onEditCommit: Nothing? = null

    init {
        if (isEditable)
            setOnEditCommit {
                if (propName != null) {
                    val item = tableView.selectionModel.selectedItem!!
                    val field = item.javaClass.getDeclaredField(propName!!)
                    field.isAccessible = true
                    when (field.get(item)) {
                        is String -> field.set(item, it.newValue)
                        is Int -> field.set(item, it.newValue.toIntOrNull() ?: 0)
                        is Double -> field.set(item, it.newValue.toDoubleOrNull() ?: 0.0)
                    }

                    tableView.refresh()
                }

                if (onEditCommitted != null) {
                    onEditCommitted!!.handle(it)

                    tableView.refresh()
                }
            }
    }
}