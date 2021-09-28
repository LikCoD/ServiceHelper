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
import ldcapps.servicehelper.Dialogs
import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.db.DataClasses.Companion.cars
import ldcapps.servicehelper.db.DataClasses.Companion.companies
import ldcapps.servicehelper.db.DataClasses.Companion.individuals
import ldcapps.servicehelper.db.DataClasses.Companion.owners
import ldcapps.servicehelper.initTableSize
import ldcapps.servicehelper.toFXList
import ldclibs.javafx.controls.Column
import ldclibs.javafx.controls.MyTableCell
import java.awt.Toolkit
import java.net.URL
import java.util.*

class RedactDB : Initializable {
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

    private var cInit = false
    private var oInit = false

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        carsTable.initTableSize(1, 1, 2, 2, 1, 1, 2)

        carNumberCol.setValueFactory(DataClasses.Car::number.name)
        carShortNumberCol.setValueFactory(DataClasses.Car::keyNum.name)
        carModelCol.setValueFactory(DataClasses.Car::model.name)
        carVINCol.setValueFactory(DataClasses.Car::vin.name)
        carYearCol.setValueFactory(DataClasses.Car::year.name)
        carEngineCol.setValueFactory(DataClasses.Car::engine.name)
        carOwnerCol.setValueFactory(DataClasses.Car::owner.name)

        carNumberCol.setTextFieldCellFactory()
        carShortNumberCol.setTextFieldCellFactory()
        carModelCol.setTextFieldCellFactory()
        carVINCol.setTextFieldCellFactory()
        carYearCol.setIntTextFieldCellFactory()
        carEngineCol.setDoubleTextFieldCellFactory()
        carOwnerCol.setComboBoxCellFactory(*(companies.map { it.company } + owners.map { it.owner } + individuals.map { it.individual }).toTypedArray())

        carModelCol.setCellFactory {
            MyTableCell<DataClasses.Car>()
        }

        carsTable.items = cars.toFXList()

        carsTb.setOnAction {
            carsTable.initTableSize(1, 1, 2, 2, 1, 1, 2)

            carsTable.isVisible = true
            companiesTable.isVisible = false
            ownersAndIndividualsAp.isVisible = false
        }
        companiesTb.setOnAction {
            companiesTable.initTableSize(2, 2, 2, 1, 1, 1)

            if (!cInit) {
                companyCol.setValueFactory(DataClasses.Company::company.name)
                companyAddressCol.setValueFactory(DataClasses.Company::address.name)
                companyPACol.setValueFactory(DataClasses.Company::pa.name)
                companyBIKCol.setValueFactory(DataClasses.Company::bik.name)
                companyPRNCol.setValueFactory(DataClasses.Company::prn.name)
                companyContractDateCol.setValueFactory(DataClasses.Company::contractDate.name)
                companyCol.setTextFieldCellFactory()
                companyAddressCol.setTextFieldCellFactory()
                companyPACol.setTextFieldCellFactory()
                companyBIKCol.setTextFieldCellFactory()
                companyPRNCol.setIntTextFieldCellFactory()
                companyContractDateCol.setTextFieldCellFactory()
                companiesTable.items = companies.toFXList()
                cInit = true
            }

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

            if (!oInit) {
                ownerCol.setValueFactory(DataClasses.Owner::owner.name)
                ownerCompanyCol.setValueFactory(DataClasses.Owner::company.name)
                individualCol.setValueFactory(DataClasses.Individual::individual.name)
                individualAddressCol.setValueFactory(DataClasses.Individual::address.name)
                ownerCol.setTextFieldCellFactory()
                ownerCompanyCol.setTextFieldCellFactory()
                individualCol.setTextFieldCellFactory()
                individualAddressCol.setTextFieldCellFactory()
                ownersTable.items = owners.toFXList()
                individualsTable.items = individuals.toFXList()
                oInit = true
            }

            carsTable.isVisible = false
            companiesTable.isVisible = false
            ownersAndIndividualsAp.isVisible = true
        }
        confirmBtn.setOnAction {
            Dialogs.confirmation("БД успешно отредактированно") {
                if (!cInit) {
                    companyCol.setValueFactory(DataClasses.Company::company.name)
                    companyAddressCol.setValueFactory(DataClasses.Company::address.name)
                    companyPACol.setValueFactory(DataClasses.Company::pa.name)
                    companyBIKCol.setValueFactory(DataClasses.Company::bik.name)
                    companyPRNCol.setValueFactory(DataClasses.Company::prn.name)
                    companyContractDateCol.setValueFactory(DataClasses.Company::contractDate.name)
                    companiesTable.items = companies.toFXList()
                    cInit = true
                }
                if (!oInit) {
                    ownerCol.setValueFactory(DataClasses.Owner::owner.name)
                    ownerCompanyCol.setValueFactory(DataClasses.Owner::company.name)
                    individualCol.setValueFactory(DataClasses.Individual::individual.name)
                    individualAddressCol.setValueFactory(DataClasses.Individual::individual.name)
                    individualsTable.items = individuals.toFXList()
                    ownersTable.items = owners.toFXList()
                    oInit = true
                }
                when {
                    carsTable.isVisible -> Dialogs.print(
                        ToolSelector.toolStage, PageOrientation.LANDSCAPE,
                        carsTable, companiesTable, ownersAndIndividualsAp
                    )
                    companiesTable.isVisible -> Dialogs.print(
                        ToolSelector.toolStage, PageOrientation.LANDSCAPE,
                        companiesTable, carsTable, ownersAndIndividualsAp
                    )
                    else -> Dialogs.print(
                        ToolSelector.toolStage,
                        PageOrientation.LANDSCAPE,
                        ownersAndIndividualsAp,
                        carsTable,
                        companiesTable
                    )
                }
            }

            Panes.REDACT_DB.update()
        }
    }

    fun changeCarNumberCellEvent(editEvent: TableColumn.CellEditEvent<DataClasses.Car, String>) {
        carsTable.selectionModel.selectedItem.number = editEvent.newValue!!
    }

    fun changeShortCarNumberCellEvent(editEvent: TableColumn.CellEditEvent<DataClasses.Car, String>) {
        carsTable.selectionModel.selectedItem.keyNum = editEvent.newValue!!
    }

    fun changeCarModelCellEvent(editEvent: TableColumn.CellEditEvent<DataClasses.Car, String>) {
        carsTable.selectionModel.selectedItem.model = editEvent.newValue!!
    }

    fun changeCarVINCellEvent(editEvent: TableColumn.CellEditEvent<DataClasses.Car, String>) {
        carsTable.selectionModel.selectedItem.vin = editEvent.newValue!!
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

    fun changeCarOwnerCellEvent(editEvent: TableColumn.CellEditEvent<DataClasses.Car, String>) {
        carsTable.selectionModel.selectedItem.owner = editEvent.newValue!!
    }

    fun changeCompanyCellEvent(editEvent: TableColumn.CellEditEvent<DataClasses.Company, String>) {
        cars.filter { it.owner == companiesTable.selectionModel.selectedItem.company }
            .forEach { cars[cars.indexOf(it)].owner = editEvent.newValue }
        carsTable.items = cars.toFXList()
        companiesTable.selectionModel.selectedItem.company = editEvent.newValue
    }

    fun changeCompanyAddressCellEvent(editEvent: TableColumn.CellEditEvent<DataClasses.Company, String>) {
        companiesTable.selectionModel.selectedItem.address = editEvent.newValue
    }

    fun changeCompanyPACellEvent(editEvent: TableColumn.CellEditEvent<DataClasses.Company, String>) {
        companiesTable.selectionModel.selectedItem.pa = editEvent.newValue
    }

    fun changeCompanyBIKCellEvent(editEvent: TableColumn.CellEditEvent<DataClasses.Company, String>) {
        companiesTable.selectionModel.selectedItem.bik = editEvent.newValue
    }

    fun changeCompanyPRNCellEvent(editEvent: TableColumn.CellEditEvent<DataClasses.Company, String>) {
        if (editEvent.newValue.toIntOrNull() != null)
            companiesTable.selectionModel.selectedItem.prn = editEvent.newValue
        else Dialogs.warning("Данные введены неправильно")
    }

    fun changeCompanyContractDateCellEvent(editEvent: TableColumn.CellEditEvent<DataClasses.Company, String>) {
        companiesTable.selectionModel.selectedItem.contractDate = editEvent.newValue
    }

    fun changeOwnerCellEvent(editEvent: TableColumn.CellEditEvent<DataClasses.Owner, String>) {
        cars.filter { it.owner == ownersTable.selectionModel.selectedItem.owner }
            .forEach { cars[cars.indexOf(it)].owner = editEvent.newValue }
        carsTable.items = cars.toFXList()
        ownersTable.selectionModel.selectedItem.owner = editEvent.newValue
    }

    fun changeOwnerCompanyCellEvent(editEvent: TableColumn.CellEditEvent<DataClasses.Owner, String>) {
        ownersTable.selectionModel.selectedItem.company = editEvent.newValue
    }

    fun changeIndividualCellEvent(editEvent: TableColumn.CellEditEvent<DataClasses.Individual, String>) {
        cars.filter { it.owner == individualsTable.selectionModel.selectedItem.individual }
            .forEach { cars[cars.indexOf(it)].owner = editEvent.newValue }
        carsTable.items = cars.toFXList()
        individualsTable.selectionModel.selectedItem.individual = editEvent.newValue
    }

    fun changeIndividualAddressCellEvent(editEvent: TableColumn.CellEditEvent<DataClasses.Individual, String>) {
        individualsTable.selectionModel.selectedItem.address = editEvent.newValue
    }

    fun onKeyReleasedRedactCar(event: KeyEvent) = deletePos(event, cars)

    fun onKeyReleasedRedactCompany(event: KeyEvent) =
        deletePos(event, companies) {
            cars.removeAll { it.owner == companiesTable.selectionModel.selectedItem.company }
            carsTable.items = cars.toFXList()
        }

    fun onKeyReleasedRedactOwner(event: KeyEvent) =
        deletePos(event, owners) {
            cars.removeAll { it.owner == ownersTable.selectionModel.selectedItem.owner }
            carsTable.items = cars.toFXList()
        }

    fun onKeyReleasedRedactIndividual(event: KeyEvent) =
        deletePos(event, individuals) {
            cars.removeAll { it.owner == individualsTable.selectionModel.selectedItem.individual }
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
            else Dialogs.warning("Выберите позицию для удаления")
    }
}