package ldcapps.servicehelper.db

import ldcapps.servicehelper.arrFromJSON
import ldcapps.servicehelper.fromJSON
import ldcapps.servicehelper.settings
import java.io.File
import java.time.LocalDate

sealed class DataClasses {

    data class Date(var year: Int? = null, var month: Int? = null, var day: Int? = null) {
        override fun toString() = "$year-$month-$day"

        fun get() = "${if (day!! < 10) "0$day" else day}.${if (month!! < 10) "0$month" else month}.$year"

        fun toLocalDate() =
            if (year != 0 && month != null && day != null) LocalDate.of(year!!, month!!, day!!) else null

        constructor(date: LocalDate) : this(date.year, date.monthValue, date.dayOfMonth)

        constructor(date: java.sql.Date) : this(
            date.toString().substring(0..3).toIntOrNull(),
            date.toString().substring(5..6).toIntOrNull(),
            date.toString().substring(8..9).toIntOrNull()
        )

        constructor(date: String) : this(
            date.substring(6..9).toIntOrNull(),
            date.substring(3..4).toIntOrNull(),
            date.substring(0..1).toIntOrNull(),
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
        val dpcCount: Int = 0
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
        val workers: MutableList<String> = mutableListOf()
    )

    data class Car(
        var number: String = "",
        var keyNum: String = "",
        var model: String = "",
        var vin: String = "",
        var year: String = "",
        var engine: String = "",
        var owner: String = ""
    )

    data class Company(
        var company: String = "",
        var address: String = "",
        var pa: String = "",
        var bank: String = "",
        var bik: String = "",
        var prn: String = "",
        var contractDate: String = ""
    )

    data class Owner(
        var owner: String = "",
        var company: String = ""
    )

    data class Individual(
        var individual: String = "",
        var address: String = ""
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