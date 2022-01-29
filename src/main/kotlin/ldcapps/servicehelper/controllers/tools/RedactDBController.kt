package ldcapps.servicehelper.controllers.tools

import javafx.fxml.Initializable
import javafx.print.PageOrientation
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.ToggleButton
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import kotlinx.serialization.ExperimentalSerializationApi
import ldcapps.servicehelper.Dialogs
import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.db.DataClasses.Companion.cars
import ldcapps.servicehelper.db.DataClasses.Companion.companies
import ldcapps.servicehelper.db.DataClasses.Companion.individuals
import ldcapps.servicehelper.db.DataClasses.Companion.owners
import ldcapps.servicehelper.initTableSize
import ldcapps.servicehelper.toFXList
import ldcapps.servicehelper.toLocalString
import ldclibs.javafx.controls.Column
import ldclibs.javafx.controls.MyTableCell
import java.awt.Toolkit
import java.net.URL
import java.util.*

@ExperimentalSerializationApi
class RedactDBController : Initializable {
    lateinit var confirmBtn: Button
    lateinit var carsTb: ToggleButton
    lateinit var companiesTb: ToggleButton
    lateinit var ownersAndIndividualsTb: ToggleButton
    lateinit var carsTable: TableView<DataClasses.Car>
    lateinit var carNumberCol: Column<DataClasses.Car>
    lateinit var carShortNumberCol: Column<DataClasses.Car>
    lateinit var carModelCol: Column<DataClasses.Car>
    lateinit var carVINCol: Column<DataClasses.Car>
    lateinit var carYearCol: Column<DataClasses.Car>
    lateinit var carEngineCol: Column<DataClasses.Car>
    lateinit var carOwnerCol: Column<DataClasses.Car>
    lateinit var companiesTable: TableView<DataClasses.Company>
    lateinit var companyCol: Column<DataClasses.Company>
    lateinit var companyAddressCol: Column<DataClasses.Company>
    lateinit var companyPACol: Column<DataClasses.Company>
    lateinit var companyBIKCol: Column<DataClasses.Company>
    lateinit var companyPRNCol: Column<DataClasses.Company>
    lateinit var companyContractDateCol: Column<DataClasses.Company>
    lateinit var ownersAndIndividualsAp: AnchorPane
    lateinit var ownersTable: TableView<DataClasses.Owner>
    lateinit var ownerCol: Column<DataClasses.Owner>
    lateinit var ownerCompanyCol: Column<DataClasses.Owner>
    lateinit var individualsTable: TableView<DataClasses.Individual>
    lateinit var individualCol: Column<DataClasses.Individual>
    lateinit var individualAddressCol: Column<DataClasses.Individual>

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        carsTable.initTableSize(1, 1, 2, 2, 1, 1, 2)

        carNumberCol.setValueFactory(DataClasses.Car::number.name)
        carShortNumberCol.setValueFactory(DataClasses.Car::key.name)
        carModelCol.setValueFactory(DataClasses.Car::model.name)
        carVINCol.setValueFactory(DataClasses.Car::vin.name)
        carYearCol.setValueFactory(DataClasses.Car::year.name)
        carEngineCol.setValueFactory(DataClasses.Car::engine.name)
        carOwnerCol.setValueFactory { car ->
            when {
                car.companyId != null -> companies.find { it.id == car.companyId }?.company ?: ""
                car.ownerId != null -> owners.find { it.id == car.ownerId }?.owner ?: ""
                car.individualId != null -> individuals.find { it.id == car.individualId }?.individual ?: ""
                else -> ""
            }
        }
        companyCol.setValueFactory(DataClasses.Company::company.name)
        companyAddressCol.setValueFactory(DataClasses.Company::address.name)
        companyPACol.setValueFactory(DataClasses.Company::pa.name)
        companyBIKCol.setValueFactory(DataClasses.Company::swift.name)
        companyPRNCol.setValueFactory(DataClasses.Company::accountNumber.name)
        companyContractDateCol.setValueFactory { it.contractDate.toLocalString() }

        ownerCol.setValueFactory(DataClasses.Owner::owner.name)
        ownerCompanyCol.setValueFactory {owner ->
            companies.find { it.id == owner.companyId }?.company ?: ""
        }
        individualCol.setValueFactory(DataClasses.Individual::individual.name)
        individualAddressCol.setValueFactory(DataClasses.Individual::address.name)

        carNumberCol.setTextFieldCellFactory()
        carShortNumberCol.setTextFieldCellFactory()
        carModelCol.setTextFieldCellFactory()
        carVINCol.setTextFieldCellFactory()
        carYearCol.setIntTextFieldCellFactory()
        carEngineCol.setDoubleTextFieldCellFactory()
        carOwnerCol.setComboBoxCellFactory(*(companies.map { it.company } + owners.map { it.owner } + individuals.map { it.individual }).toTypedArray())

        companyCol.setTextFieldCellFactory()
        companyAddressCol.setTextFieldCellFactory()
        companyPACol.setTextFieldCellFactory()
        companyBIKCol.setTextFieldCellFactory()
        companyPRNCol.setIntTextFieldCellFactory()
        companyContractDateCol.setTextFieldCellFactory()
        ownerCol.setTextFieldCellFactory()
        ownerCompanyCol.setTextFieldCellFactory()
        individualCol.setTextFieldCellFactory()
        individualAddressCol.setTextFieldCellFactory()

        carModelCol.setCellFactory {
            MyTableCell<DataClasses.Car>()
        }

        carsTable.items = cars.toFXList()
        companiesTable.items = companies.toFXList()
        ownersTable.items = owners.toFXList()
        individualsTable.items = individuals.toFXList()

        carsTb.setOnAction {
            carsTable.initTableSize(1, 1, 2, 2, 1, 1, 2)

            carsTable.isVisible = true
            companiesTable.isVisible = false
            ownersAndIndividualsAp.isVisible = false
        }
        companiesTb.setOnAction {
            companiesTable.initTableSize(2, 2, 2, 1, 1, 1)

            carsTable.isVisible = false
            companiesTable.isVisible = true
            ownersAndIndividualsAp.isVisible = false
        }
        ownersAndIndividualsTb.setOnAction {
            val w = Toolkit.getDefaultToolkit().screenSize.width.toDouble() - 340
            val h = Toolkit.getDefaultToolkit().screenSize.height.toDouble() - 100

            ownersTable.prefWidth = (if (w < h * (351.0 / 248.0)) w else h * (351.0 / 248.0)) / 2
            ownersTable.prefHeight = ownersTable.prefWidth * 2 / (351.0 / 248.0)
            ownersTable.columns[0].columns.forEach { it.prefWidth = ownersTable.prefWidth / 2 }
            individualsTable.prefWidth = (if (w < h * (351.0 / 248.0)) w else h * (351.0 / 248.0)) / 2
            individualsTable.prefHeight = individualsTable.prefWidth * 2 / (351.0 / 248.0)
            individualsTable.columns[0].columns.forEach { it.prefWidth = individualsTable.prefWidth / 2 }
            individualsTable.layoutX = ownersTable.prefWidth

            carsTable.isVisible = false
            companiesTable.isVisible = false
            ownersAndIndividualsAp.isVisible = true
        }
        confirmBtn.setOnAction {
            Dialogs.confirmation("БД успешно отредактированно") {
                when {
                    carsTable.isVisible -> Dialogs.print(
                        confirmBtn.scene.window as Stage, PageOrientation.LANDSCAPE,
                        carsTable, companiesTable, ownersAndIndividualsAp
                    )
                    companiesTable.isVisible -> Dialogs.print(
                        confirmBtn.scene.window as Stage, PageOrientation.LANDSCAPE,
                        companiesTable, carsTable, ownersAndIndividualsAp
                    )
                    else -> Dialogs.print(
                        confirmBtn.scene.window as Stage,
                        PageOrientation.LANDSCAPE,
                        ownersAndIndividualsAp,
                        carsTable,
                        companiesTable
                    )
                }
            }

            ToolsController.REDACT_DB.update()
        }
    }

    fun changeCarYearCellEvent(editEvent: TableColumn.CellEditEvent<DataClasses.Car, String>) {
        if (editEvent.newValue.toIntOrNull() != null)
            carsTable.selectionModel.selectedItem.year = editEvent.newValue.toIntOrNull() ?: 0
        else Dialogs.warning("Данные введены неправильно")
    }

    fun changeCarEngineCellEvent(editEvent: TableColumn.CellEditEvent<DataClasses.Car, String>) {
        if (editEvent.newValue.toDoubleOrNull() != null)
            carsTable.selectionModel.selectedItem.engine = editEvent.newValue.toDoubleOrNull() ?: 0.0
        else Dialogs.warning("Данные введены неправильно")
    }

    fun changeCompanyCellEvent(editEvent: TableColumn.CellEditEvent<DataClasses.Company, String>) {
        carsTable.items = cars.toFXList()
        companiesTable.selectionModel.selectedItem.company = editEvent.newValue
    }

/*    fun changeCompanyPRNCellEvent(editEvent: TableColumn.CellEditEvent<DataClasses.Company, String>) {
        if (editEvent.newValue.toIntOrNull() != null)
            companiesTable.selectionModel.selectedItem.accountNumber = editEvent.newValue.toInt()
        else Dialogs.warning("Данные введены неправильно")
    }*/

    fun changeOwnerCellEvent(editEvent: TableColumn.CellEditEvent<DataClasses.Owner, String>) {
        carsTable.items = cars.toFXList()
        ownersTable.selectionModel.selectedItem.owner = editEvent.newValue
    }

    fun changeIndividualCellEvent(editEvent: TableColumn.CellEditEvent<DataClasses.Individual, String>) {
        carsTable.items = cars.toFXList()
        individualsTable.selectionModel.selectedItem.individual = editEvent.newValue
    }


    fun onKeyReleasedRedactCar(event: KeyEvent) = deletePos(event, cars)

    fun onKeyReleasedRedactCompany(event: KeyEvent) =
        deletePos(event, companies) {
            cars.removeAll { it.companyId == companiesTable.selectionModel.selectedItem.id }
            carsTable.items = cars.toFXList()
        }

    fun onKeyReleasedRedactOwner(event: KeyEvent) =
        deletePos(event, owners) {
            cars.removeAll { it.ownerId == ownersTable.selectionModel.selectedItem.id }
            carsTable.items = cars.toFXList()
        }

    fun onKeyReleasedRedactIndividual(event: KeyEvent) =
        deletePos(event, individuals) {
            cars.removeAll { it.individualId == individualsTable.selectionModel.selectedItem.id }
            carsTable.items = cars.toFXList()
        }

    private inline fun deletePos(event: KeyEvent, list: MutableList<*>, crossinline ifDel: () -> Unit = {}) {
        if (event.code == KeyCode.DELETE && event.isControlDown)
            if ((event.source as TableView<*>).selectionModel.selectedItem != null)
                Dialogs.confirmation("Подтвердите удаление") {
                    ifDel()
                    list.remove((event.source as TableView<*>).selectionModel.selectedItem)
                    (event.source as TableView<*>).items = list.toFXList()
                }
    }
}