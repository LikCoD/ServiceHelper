package ldcapps.servicehelper.controllers

import ldcapps.servicehelper.Windows
import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.db.Orders
import ldcapps.servicehelper.settings
import ldcapps.servicehelper.styles.MainStyle
import ldcapps.servicehelper.toJSON
import liklibs.db.toSQL
import tornadofx.*
import java.io.File
import java.time.LocalDate

class ExportView : View() {
    override val root = vbox(10) {
        addClass(MainStyle.mainWindow)

        button("Export") {
            action {
                val exportDir = File(settings.oosLocate, "Export\\${LocalDate.now().toSQL()}")
                exportDir.mkdirs()

                Orders.ordersAndBills.forEach {
                    val ooAndBill = it.toOOAndBill() ?: return@forEach

                    val name =
                        "Заказ-Наряд №${ooAndBill.number} от ${ooAndBill.registrationDate} от ${ooAndBill.customer?.company}.oab"

                    toJSON(File(exportDir, name), ooAndBill)
                }
            }
        }

        tableview(Orders.ordersAndBills.asObservable()) {
            readonlyColumn("#", Orders.OrderAndBill::number)
            readonlyColumn("Registration date", Orders.OrderAndBill::registrationDate)
            readonlyColumn("Execution date", Orders.OrderAndBill::executionDate)
            readonlyColumn("Car", Orders.OrderAndBill::carId).cellFormat { id ->
                val car = DataClasses.cars.find { it.id == id }
                if (car == null) {
                    text = "-"
                    return@cellFormat
                }
                text = "${car.number} ${car.model}"
            }
            readonlyColumn("Car mileage", Orders.OrderAndBill::carMileage)
            readonlyColumn("Customer", Orders.OrderAndBill::customerId).cellFormat { id ->
                val customer = DataClasses.companies.find { it.id == id }
                if (customer == null) {
                    text = "-"
                    return@cellFormat
                }
                text = customer.company
            }
            readonlyColumn("Owner", Orders.OrderAndBill::owner)
            readonlyColumn("Works size", Orders.OrderAndBill::works).cellFormat { text = it.size.toString() }
            readonlyColumn("Dpcs size", Orders.OrderAndBill::dpcs).cellFormat { text = it.size.toString() }
            readonlyColumn("Dfc size", Orders.OrderAndBill::dfcs).cellFormat { text = it.size.toString() }

            onSelectionChange { order ->
                if (order == null) return@onSelectionChange

                val ooAndBill = order.toOOAndBill() ?: return@onSelectionChange
                Windows.mainController.fillOO(ooAndBill, id = order.id)
            }
        }
    }
}