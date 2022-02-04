package ldcapps.servicehelper.db

import ldcapps.servicehelper.arrFromJSON
import ldcapps.servicehelper.fromJSON
import liklibs.db.Date
import liklibs.db.annotations.*
import liklibs.db.delegates.DBProperty.dbProperty
import liklibs.db.sqList

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
        val number by dbProperty(number)
        val type by dbProperty(type)
        val executor by dbProperty(executor)
        val customer by dbProperty(customer)
        val owner by dbProperty(owner)
        val carNumber by dbProperty(carNumber)
        val carMileage by dbProperty(carMileage)

        @DBField("registrationDate")
        val regDate by dbProperty(regDate)

        @DBField("executionDate")
        val exDate by dbProperty(exDate)
        val hourNorm by dbProperty(hourNorm)
        val totalWorkPrice by dbProperty(totalWorkPrice)
        val totalDPCPrice by dbProperty(totalDPCPrice)
        val vat by dbProperty(vat)
        val workCount by dbProperty(workCount)
        val dfcCount by dbProperty(dfcCount)
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
    ) {
        var number by dbProperty(number)

        @DBField("key_")
        var key by dbProperty(key)
        var model by dbProperty(model)
        var vin by dbProperty(vin)
        var year by dbProperty(year)
        var engine by dbProperty(engine)
        var ownerId by dbProperty(ownerId)
        var companyId by dbProperty(companyId)
        var individualId by dbProperty(individualId)

        @Primary
        var id by dbProperty(0)
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
        contractDate: Date
    ) {
        var company by dbProperty(company)
        var address by dbProperty(address)

        @DBField("paymentAccount")
        var pa by dbProperty(pa)
        var bank by dbProperty(bank)
        var bankAddress by dbProperty(bankAddress)
        var swift by dbProperty(swift)
        var accountNumber by dbProperty(accountNumber)
        var contractDate by dbProperty(contractDate)

        @Primary
        @Dependency("companyId", "Car")
        var id by dbProperty(0)
    }

    @DBTable("owners")
    class Owner(
        owner: String,
        companyId: Int
    ) {
        var owner by dbProperty(owner)
        var companyId by dbProperty(companyId)

        @Primary
        @Dependency("ownerId", "Car")
        var id by dbProperty(0)
    }

    @DBTable("individuals")
    class Individual(
        individual: String = "",
        address: String = ""
    ) {
        var individual by dbProperty(individual)
        var address by dbProperty(address)

        @Primary
        @Dependency("individualId", "Car")
        var id by dbProperty(0)
    }

    data class ExcelTabs(val topMargin: Int = 1, val rightMargin: Int = 0, val tabsSequence: List<String>? = null)

    companion object {
        val cars = sqList<Car>()
        val companies = sqList<Company>()
        val owners = sqList<Owner>()
        val individuals = sqList<Individual>()

        val reports = sqList<Report>()

        var excelTabs = arrFromJSON<ExcelTabs>(".excelTabs")

        var user = fromJSON<User>(".user")

        init {
            while (excelTabs.size < 3) excelTabs.add(ExcelTabs())
        }
    }

}