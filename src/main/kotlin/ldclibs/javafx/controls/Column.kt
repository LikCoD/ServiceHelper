package ldclibs.javafx.controls

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.ComboBoxTableCell
import javafx.scene.control.cell.PropertyValueFactory
import javafx.util.converter.DefaultStringConverter

class Column<S> : TableColumn<S, String>() {
    fun setValueFactory(name: String) {
        cellValueFactory = PropertyValueFactory(name)
    }

    inline fun setValueFactory(crossinline value: (S) -> Any) =
        setCellValueFactory { SimpleStringProperty(value(it.value).toString()) }

    inline fun setPriceValueFactory(crossinline value: (S) -> Double?) =
        setCellValueFactory {
            val res = value(it.value)
            if (res == null)
                SimpleStringProperty("-")
            else
                SimpleStringProperty(String.format("%.2f", res).replace(",", "."))
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
}