package ldcapps.servicehelper.db

import ldcapps.servicehelper.arrFromJSON
import ldcapps.servicehelper.fromJSON
import ldcapps.servicehelper.settings
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed class DataClasses {

    data class Date(val year: Int = 0, val month: Int = 0, val day: Int = 0) {
        val localDate: LocalDate
            get() = LocalDate.of(year, month, day)

        val value: String
            get() = DateTimeFormatter.ofPattern("dd.MM.yyyy").format(localDate).format()

        override fun toString() = "$year-$month-$day"

        constructor(date: LocalDate) : this(date.year, date.monthValue, date.dayOfMonth)

        constructor(date: java.sql.Date) : this(
            date.toString().substring(0..3).toIntOrNull() ?: 0,
            date.toString().substring(5..6).toIntOrNull() ?: 0,
            date.toString().substring(8..9).toIntOrNull() ?: 0
        )

    }

    data class Report(
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

    data class Car(
        var number: String = "",
        var keyNum: String = "",
        var model: String = "",
        var vin: String = "",
        var year: Int = 0,
        var engine: Double = 0.0,
        var owner: String = "",
    )

    data class Company(
        var company: String = "",
        var address: String = "",
        var pa: String = "",
        var bank: String = "",
        var bik: String = "",
        var prn: String = "",
        var contractDate: String = "",
    )

    data class Owner(
        var owner: String = "",
        var company: String = "",
    )

    data class Individual(
        var individual: String = "",
        var address: String = "",
    )

    data class ExcelTabs(val topMargin: Int = 1, val rightMargin: Int = 0, val tabsSequence: List<String>? = null)

    companion object {
        init {
            MySqlDb.set(settings.host, settings.port, settings.login, settings.password)
        }

        var db: MySqlDb? = try {
            MySqlDb(settings.dbName)
        } catch (ex: Exception) {
            null
        }

        var reports = SQList(db, "reports", Report(), arrFromJSON(".report"), ".report")
        var cars = SQList(db, "cars", Car(), arrFromJSON(".cars"), ".cars")
        var companies = SQList(db, "companies", Company(), arrFromJSON(".companies"), ".companies")
        var owners = SQList(db, "owners", Owner(), arrFromJSON(".owners"), ".owners")
        var individuals = SQList(db, "individuals", Individual(), arrFromJSON(".individuals"), ".individuals")

        var excelTabs = arrFromJSON<ExcelTabs>(".excelTabs")

        var user = db?.getUser(User()) ?: fromJSON(".user")

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