package ldcapps.servicehelper.db

import kotlinx.serialization.ExperimentalSerializationApi
import ldcapps.servicehelper.arrFromJSON
import ldcapps.servicehelper.fromJSON
import java.io.File
import kotlinx.serialization.Serializable
import liklibs.db.*

sealed class DataClasses {

    data class Report(
        val user: String = "",
        val number: Int = 0,
        val type: Int = 0,
        val executor: String = "",
        val customer: String = "",
        val owner: String = "",
        val carNumber: String = "",
        val carMileage: Int? = null,
        val regDate: Date = Date(0, 0, 0),
        val exDate: Date = Date(0, 0, 0),
        val hourNorm: Double = 0.0,
        val totalWorkPrice: Double = 0.0,
        val totalDPCPrice: Double = 0.0,
        val vat: Double? = null,
        val workCount: Int = 0,
        val dfcCount: Int = 0,
        val dpcCount: Int = 0,
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

    @DBInfo(
        dbName = "defriuiuqmjmcl",
        tableName = "cars",
    )
    @Serializable
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
        @NotInsertable @DBField("_id") var id: Int = -1
    )

    @DBInfo(
        dbName = "defriuiuqmjmcl",
        tableName = "companies",
    )
    @Serializable
    data class Company(
        var company: String = "",
        var address: String = "",
        @DBField("paymentAccount") var pa: String = "",
        var bank: String = "",
        var swift: String = "",
        var accountNumber: Int = -1,
        var contractDate: Date = Date(),
        @NotInsertable @DBField("_id") var id: Int = -1
    )

    @DBInfo(
        dbName = "defriuiuqmjmcl",
        tableName = "owners",
    )
    @Serializable
    data class Owner(
        var owner: String = "",
        var companyId: Int = -1,
        @NotInsertable @DBField("_id") var id: Int = -1
    )

    @DBInfo(
        dbName = "defriuiuqmjmcl",
        tableName = "individuals",
    )
    @Serializable
    data class Individual(
        var individual: String = "",
        var address: String = "",
        @NotInsertable @DBField("_id") var id: Int = -1
    )

    data class ExcelTabs(val topMargin: Int = 1, val rightMargin: Int = 0, val tabsSequence: List<String>? = null)

    @ExperimentalSerializationApi
    companion object {
        val cars = sqList<Car>("db_credentials.json")
        val companies = sqList<Company>("db_credentials.json")
        val owners = sqList<Owner>("db_credentials.json")
        val individuals = sqList<Individual>("db_credentials.json")

        var reports = SQList(null, "reports", Report(), arrFromJSON(".report"), ".report")

        var excelTabs = arrFromJSON<ExcelTabs>(".excelTabs")

        var user = fromJSON<User>(".user")

        fun delete() {
            File(".report").delete()
            File(".companies").delete()
            File(".cars").delete()
            File(".individuals").delete()
            File(".owners").delete()
            File(".data").delete()
            File(".excelTabs").delete()
            File(".settings").delete()
            File(".user").delete()
            File("token.key").delete()
        }

        init {
            while (excelTabs.size < 3) excelTabs.add(ExcelTabs())
        }
    }

}