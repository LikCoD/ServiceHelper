package ldcapps.servicehelper.db

import ldcapps.servicehelper.Name
import ldcapps.servicehelper.arrFromJSON
import ldcapps.servicehelper.fromJSON
import liklibs.db.Date
import liklibs.db.SQList
import liklibs.db.annotations.*
import liklibs.db.delegates.dbDependency
import liklibs.db.delegates.dbProperty

@DBInfo("defriuiuqmjmcl", "db_credentials.json")
sealed class DataClasses {

    @DBTable("reports")
    class Report(
        number: Int,
        type: Int,
        executor: String,
        customer: String,
        owner: String,
        carNumber: String,
        carMileage: Int?,
        regDate: Date,
        exDate: Date,
        hourNorm: Double,
        totalWorkPrice: Double,
        totalDPCPrice: Double,
        vat: Double?,
        workCount: Int,
        dfcCount: Int,
        dpcCount: Int
    ) {
        @Name("№")
        val number by dbProperty(number)

        @Name("Тип")
        val type by dbProperty(type)

        @Name("Исполнитель")
        val executor by dbProperty(executor)

        @Name("Заказчик")
        val customer by dbProperty(customer)

        @Name("Владелец")
        val owner by dbProperty(owner)

        @Name("Номер авто")
        val carNumber by dbProperty(carNumber)

        @Name("Пробег")
        val carMileage by dbProperty(carMileage)

        @Name("Дата оформления")
        @DBField("registrationDate")
        val regDate by dbProperty(regDate)

        @Name("Дата исполнения")
        @DBField("executionDate")
        val exDate by dbProperty(exDate)

        @Name("Н/ч")
        val hourNorm by dbProperty(hourNorm)

        @Name("Работа")
        val totalWorkPrice by dbProperty(totalWorkPrice)

        @Name("Детали")
        val totalDPCPrice by dbProperty(totalDPCPrice)

        @Name("НДС")
        val vat by dbProperty(vat)

        @Name("Работы")
        val workCount by dbProperty(workCount)

        @Name("Детали (от)")
        val dfcCount by dbProperty(dfcCount)

        @Name("Детали")
        val dpcCount by dbProperty(dpcCount)

        @Primary
        var id by dbProperty(0)
    }

    data class User(
        val name: String = "",
        val hourNorm: Double = 0.0,
        var vat: Double? = null,
        val standardUnit: String = "",
        val standardState: String = "",
        val standardWorker: String = "",
        val executor: String = "",
        val abbreviatedExecutor: String = "",
        val address: String = "",
        val serviceAddress: String = "",
        val pa: String = "",
        val bank: String = "",
        val bankAddress: String = "",
        val prn: Int = 0,
        val bik: String = "",
        val phone: String = "",
        val email: String = "",
        val footing: String = "",
        val workers: MutableList<String> = mutableListOf(),
    ) {
        fun getExecutor() =
            Executor(vat, hourNorm, executor, abbreviatedExecutor, address, pa, bank, bankAddress, prn, bik, phone)
    }

    data class Executor(
        var vat: Double? = null,
        var hourNorm: Double = 0.0,
        var executor: String = "",
        var abbreviatedExecutor: String = "",
        var address: String = "",
        var pa: String = "",
        var bank: String = "",
        var bankAddress: String = "",
        var prn: Int = 0,
        var bik: String = "",
        var phone: String = "",
    )

    @DBTable("cars")
    class Car(
        number: String,
        key: String,
        model: String,
        vin: String,
        year: Int,
        engine: Double,
        ownerId: Int?,
        companyId: Int?,
        individualId: Int?,
        id: Int? = 0,
    ) {
        @Name("№")
        var number by dbProperty(number)

        @Name("Ключ")
        @DBField("key_")
        var key by dbProperty(key)
        var model by dbProperty(model)

        @Name("VIN")
        var vin by dbProperty(vin)

        @Name("Год")
        var year by dbProperty(year)

        @Name("Двигатель")
        var engine by dbProperty(engine)

        @Name("id владельца")
        @Dependency("owners", "_id")
        var ownerId by dbProperty(ownerId)

        @Name("id компании")
        @Dependency("companies", "_id")
        var companyId by dbProperty(companyId)

        @Name("id физ. лица")
        @Dependency("individuals", "_id")
        var individualId by dbProperty(individualId)

        @Primary
        var id by dbProperty(id)
    }

    @DBTable("owners")
    class Owner(
        owner: String,
        companyId: Int,
        id: Int = 0
    ) {
        @Name("Заказчик")
        var owner by dbProperty(owner)

        @Name("id компании")
        @Dependency("companies", "_id")
        var companyId by dbProperty(companyId)

        @Primary
        var id by dbDependency(id, Car::ownerId)
    }

    @DBTable("companies")
    class Company(
        company: String,
        address: String,
        pa: String,
        bank: String,
        bankAddress: String,
        swift: String,
        accountNumber: Int,
        contractDate: Date,
        id: Int = 0
    ) {
        @Name("Компания")
        var company by dbProperty(company)

        @Name("Адрес")
        var address by dbProperty(address)

        @Name("Р/с")
        @DBField("paymentAccount")
        var pa by dbProperty(pa)

        @Name("Банк")
        var bank by dbProperty(bank)

        @Name("Адрес банка")
        var bankAddress by dbProperty(bankAddress)

        @Name("БИК")
        var swift by dbProperty(swift)

        @Name("УНП")
        var accountNumber by dbProperty(accountNumber)

        @Name("Дата договора")
        var contractDate by dbProperty(contractDate)

        @Primary
        var id by dbDependency(id, Car::companyId, Owner::companyId)
    }

    @DBTable("individuals")
    class Individual(
        individual: String = "",
        address: String = ""
    ) {
        @Name("Физ. лицо")
        var individual by dbProperty(individual)

        @Name("Адрес")
        var address by dbProperty(address)

        @Primary
        var id by dbDependency(0, Car::individualId)
    }

    data class ExcelTabs(val topMargin: Int = 1, val rightMargin: Int = 0, val tabsSequence: List<String>? = null)

    companion object {
        lateinit var companies: SQList<Company>
        lateinit var individuals: SQList<Individual>
        lateinit var owners: SQList<Owner>
        lateinit var cars: SQList<Car>

        lateinit var reports: SQList<Report>

        var excelTabs = arrFromJSON<ExcelTabs>(".excelTabs")

        var user = fromJSON<User>(".user")

        init {
            while (excelTabs.size < 3) excelTabs.add(ExcelTabs())
        }
    }

}