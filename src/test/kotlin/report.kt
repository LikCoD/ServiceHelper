/*
import ldcapps.servicehelper.Reports
import ldcapps.servicehelper.db.DBData
import ldcapps.servicehelper.reports
import ldcapps.servicehelper.toJSON
import java.sql.Date

fun main(){
    val localReports = mutableListOf<DBData.Report>()

    fun addReport(list: MutableList<MutableList<Reports.ReportMonth>>, type: Int) {
        fun getDate(date: String): Date {

            println(date)

            return Date(
                date.substring(6..9).toInt() - 1900,
                date.substring(3..4).toInt(),
                date.substring(0..1).toInt()
            )
        }

        list.forEach { report ->
            localReports.addAll(report.map {
                DBData.Report(
                    DBData.user.name,
                    it.ooNumber,
                    type,
                    it.company,
                    it.customer,
                    it.owner,
                    it.carNumber,
                    it.carMileage.toIntOrNull(),
                    getDate(it.regDate),
                    getDate(it.exDate),
                    it.hourNorm,
                    it.totalWorkPrice,
                    it.totalDPCPrice,
                    it.vat,
                    it.workPosCount,
                    it.dfcPosCount,
                    it.dpcPosCount
                )
            }
            )
        }
    }

    addReport(reports.cash, 0)
    addReport(reports.cashless, 1)

    toJSON(".re", localReports)
}*/
