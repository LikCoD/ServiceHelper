package ldcapps.servicehelper.db

import ldcapps.servicehelper.Name
import ldcapps.servicehelper.controllers.OOController
import liklibs.db.Date
import liklibs.db.SQList
import liklibs.db.annotations.DBInfo
import liklibs.db.annotations.DBTable
import liklibs.db.annotations.Primary
import liklibs.db.delegates.dbProperty

@DBInfo("d3d55jnob2jl2o", "db_oo_credentials.json")
sealed class Orders {
    @DBTable("orders_and_bills")
    class OrderAndBill(
        number: Int,
        registrationDate: Date,
        executionDate: Date,
        carId: Int,
        carMileage: Int?,
        customerId: Int,
        owner: String,
        works: List<OOController.Work>,
        dpcs: List<OOController.DPC>,
        dfcs: List<OOController.DFC>
    ) {
        @Name("№")
        var number by dbProperty(number)

        @Name("Дата оформления")
        var registrationDate by dbProperty(registrationDate)

        @Name("Дата исполнения")
        var executionDate by dbProperty(executionDate)

        @Name("id авто")
        var carId by dbProperty(carId)

        @Name("Пробег")
        var carMileage by dbProperty(carMileage)

        @Name("id заказчика")
        var customerId by dbProperty(customerId)

        @Name("Владелец")
        var owner by dbProperty(owner)

        @Name("Работы")
        var works: List<OOController.Work> by dbProperty(works)

        @Name("Детали (от)")
        var dpcs: List<OOController.DPC> by dbProperty(dpcs)

        @Name("Детали")
        var dfcs: List<OOController.DFC> by dbProperty(dfcs)

        @Primary
        var id by dbProperty(0)

        companion object {
            fun fromOOAndBill(oo: OOController.OOAndBill, id: Int? = null) {
                val order = OrderAndBill(
                    oo.number,
                    oo.registrationDate,
                    oo.executionDate,
                    oo.car?.id ?: -1,
                    oo.carMileage,
                    oo.customer?.id ?: -1,
                    oo.owner ?: "",
                    oo.works,
                    oo.dpcs,
                    oo.dfcs
                )

                if (id == null) {
                    ordersAndBills.add(order)
                    return
                }

                val orderAndBill = ordersAndBills.find { it.id == id } ?: let {
                    ordersAndBills.add(order)
                    return
                }

                orderAndBill.number = order.number
                orderAndBill.registrationDate = order.registrationDate
                orderAndBill.executionDate = order.executionDate
                orderAndBill.carId = order.carId
                orderAndBill.carMileage = order.carMileage
                orderAndBill.customerId = order.customerId
                orderAndBill.owner = order.owner
                orderAndBill.works = order.works
                orderAndBill.dpcs = order.dpcs
                orderAndBill.dfcs = order.dfcs
            }
        }

        fun toOOAndBill(): OOController.OOAndBill? {
            val car = DataClasses.cars.find { it.id == carId } ?: return null
            val customer = DataClasses.companies.find { it.id == customerId } ?: return null

            return OOController.OOAndBill(
                number,
                registrationDate,
                executionDate,
                car,
                carMileage,
                customer,
                null,
                DataClasses.user.getExecutor(),
                owner,
                works.toMutableList(),
                dpcs.toMutableList(),
                dfcs.toMutableList()
            )
        }
    }


    companion object {
        lateinit var ordersAndBills: SQList<OrderAndBill>
    }

}