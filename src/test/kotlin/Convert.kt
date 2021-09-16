import com.google.gson.Gson
import ldcapps.servicehelper.arrFromJSON
import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.fromJSON
import java.io.File
import java.sql.Date

fun main() {
    println(Convert().cars("E:\\New folder\\Программа\\Service Helper\\.cars"))
}

class Convert {

    fun date(date: String): String {
        val arr = date.replace(",", "").split(" ")
        return "{\"year\":${arr[2]},\"month\":${
            arrayOf(
                "Jan",
                "Feb",
                "Mar",
                "Apr",
                "May",
                "Jun",
                "Jul",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec",
            ).indexOf(arr[0]) + 1
        },\"day\":${arr[1]}}"
    }

    fun oo(path: String) {
        File(path).listFiles()?.forEach {
            try {
                val text = it.readText()

                println(text)

                val p1 = text.substringBefore("registrationDate")
                val p2 = text.substringAfterLast(", 2021\"")

                val d1 = text.substring(text.indexOf("registrationDate") + 19, text.indexOf(", 2021") + 6)
                val d2 = text.substring(text.indexOf("executionDate") + 19, text.lastIndexOf(", 2021") + 6)

                it.writeText("${p1}registrationDate\":${Convert().date(d1)},\"executionDate\":${Convert().date(d2)}${p2}")
            } catch (ex: Exception) {
            }
        }

    }

    fun report(path: String): String? {
        val old = fromJSON<ReportOld>(path)
        val new = mutableListOf<DataClasses.Report>()

        fun add(it: ReportOld.Report, type: Int) {
            println(it.regDate)
            println(it.exDate)


            new.add(
                DataClasses.Report(
                    "Сергей",
                    it.ooNumber,
                    type,
                    it.company,
                    it.customer,
                    it.owner,
                    it.carNumber,
                    it.carMileage.toIntOrNull(),
                    DataClasses.Date(it.regDate),
                    DataClasses.Date(it.exDate),
                    it.hourNorm,
                    it.totalWorkPrice,
                    it.totalDPCPrice,
                    null,
                    it.workPosCount,
                    it.dfcPosCount,
                    it.dpcPosCount
                )
            )
        }

        old.cash.forEach {
            it.forEach { add(it, 0) }
        }
        old.cashless.forEach {
            it.forEach { add(it, 1) }
        }

        return Gson().toJson(new)
    }

    fun cars(path: String): String? {
        val old = arrFromJSON<DataClasses.Car>(path)
        val new = mutableListOf<DataClasses.Car>()

        old.forEach {
            new.add(it)

            val number = new.last().number

            new.last().keyNum =
                if (number.substringBefore(" ").length < 4) number.substringAfter(" ").substringBefore("-").trim()
                else number.substringBefore(" ").trim()
        }

        return Gson().toJson(new)
    }

    class ReportOld {
        lateinit var cash: MutableList<MutableList<Report>>
        lateinit var cashless: MutableList<MutableList<Report>>

        class Report(
            var ooNumber: Int = 0,
            val company: String = "",
            val customer: String = "",
            val owner: String = "",
            val carNumber: String = "",
            val carMileage: String = "",
            val regDate: String = "",
            val exDate: String = "",
            val hourNorm: Double = 0.0,
            val totalWorkPrice: Double = 0.0,
            val totalDPCPrice: Double = 0.0,
            val workPosCount: Int = 0,
            val dfcPosCount: Int = 0,
            val dpcPosCount: Int = 0,
            val ooPath: String = "",
        )
    }
}