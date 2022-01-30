package ldcapps.servicehelper.db

import kotlinx.serialization.ExperimentalSerializationApi
import ldcapps.servicehelper.arrFromJSON
import ldcapps.servicehelper.fromJSON
import kotlinx.serialization.Serializable
import liklibs.db.*
import liklibs.db.annotations.DBField
import liklibs.db.annotations.DBInfo
import liklibs.db.annotations.DBTable
import liklibs.db.annotations.Primary

@ExperimentalSerializationApi
@DBInfo("defriuiuqmjmcl", "db_credentials.json")
sealed class DataClasses {

    data class ReportOld(
        val user: String = "",
        val number: Int = 0,
        val type: Int = 0,
        val executor: String = "",
        val customer: String = "",
        val owner: String = "",
        val carNumber: String = "",
        val carMileage: Int? = null,
        val regDate: Date = Date(),
        val exDate: Date = Date(),
        val hourNorm: Double = 0.0,
        val totalWorkPrice: Double = 0.0,
        val totalDPCPrice: Double = 0.0,
        val vat: Double? = null,
        val workCount: Int = 0,
        val dfcCount: Int = 0,
        val dpcCount: Int = 0,
    )

    @Serializable
    @DBTable("reports")
    data class Report(
        val number: Int = 0,
        val type: Int = 0,
        val executor: String = "",
        val customer: String = "",
        val owner: String = "",
        val carNumber: String = "",
        val carMileage: Int? = null,
        @DBField("registrationDate") val regDate: Date = Date(),
        @DBField("executionDate") val exDate: Date = Date(),
        val hourNorm: Double = 0.0,
        val totalWorkPrice: Double = 0.0,
        val totalDPCPrice: Double = 0.0,
        val vat: Double? = null,
        val workCount: Int = 0,
        val dfcCount: Int = 0,
        val dpcCount: Int = 0,
        @Primary var id: Int = -1
    )

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

    @Serializable
    @DBTable("cars")
    data class Car(
        var number: String = "",
        @DBField("key_") var key: String = "",
        var model: String = "",
        var vin: String = "",
        var year: Int = 0,
        var engine: Double = 0.0,
        var ownerId: Int? = null,
        var companyId: Int? = null,
        var individualId: Int? = null,
        @Primary var id: Int = -1
    )

    @Serializable
    @DBTable("companies")
    data class Company(
        var company: String = "",
        var address: String = "",
        @DBField("paymentAccount") var pa: String = "",
        var bank: String = "",
        var swift: String = "",
        var accountNumber: Int = -1,
        var contractDate: Date = Date(),
        @Primary var id: Int = -1
    )

    @Serializable
    @DBTable("owners")
    data class Owner(
        var owner: String = "",
        var companyId: Int = -1,
        @Primary var id: Int = -1
    )

    @Serializable
    @DBTable("individuals")
    data class Individual(
        var individual: String = "",
        var address: String = "",
        @Primary var id: Int = -1
    )

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