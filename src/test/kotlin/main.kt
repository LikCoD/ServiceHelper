import ldcapps.servicehelper.controllers.OO
import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.fromJSON
import ldcapps.servicehelper.toJSON
import java.io.File
import java.sql.Date



fun main() {

    File("P:\\ServiceHelper\\OO\\Безнал").list { _, name -> name.endsWith(".oab") || name.endsWith(".oo") }
        .forEach { oo -> fromJSON<OOAndBill>("P:\\ServiceHelper\\OO\\Безнал\\$oo").let {
            fun getDate(date: String):Date = Date(date.substring(6..9).toInt()-1900, date.substring(3..4).toInt() - 1, date.substring(0..1).toInt())
            //println("${it.registrationDate} ${getDate(it.registrationDate)}")
            toJSON(oo, OO.OOAndBill(
                it.number,
                DataClasses.Date(2020,0,0),
                DataClasses.Date(2020,0,0),
                it.carNumber,
                it.carModel,
                it.carMileage,
                it.carEngine,
                it.carYear,
                it.carVIN,
                it.customerOwner,
                it.customer,
                it.customerAddress,
                it.customerPA,
                it.customerBank,
                it.customerBIK,
                it.customerPRN,
                it.customerContractDate,
                it.vat,
                it.hourNorm,
                it.companyName,
                it.abbreviatedCompanyName,
                it.companyAddress,
                it.companyPA,
                it.companyBank,
                it.companyBankAddress,
                it.companyBIK,
                it.companyPRN,
                it.companyPhone,
                it.works,
                it.dpcs,
                it.dfcs
            )
            )
        }
    }
}


data class OOAndBill(
    var number: Int = 0,
    var registrationDate: String = "",
    var executionDate: String = "",
    var carNumber: String = "",
    var carModel: String = "",
    var carMileage: String = "",
    var carEngine: String = "",
    var carYear: String = "",
    var carVIN: String = "",
    var customerOwner: String = "",
    var customer: String = "",
    var customerAddress: String = "",
    var customerPA: String? = null,
    var customerBank: String? = null,
    var customerBIK: String? = null,
    var customerPRN: String? = null,
    var customerContractDate: String? = null,
    var vat: Double? = null,
    var hourNorm: Double = 0.0,
    var companyName: String = "",
    var abbreviatedCompanyName: String = "",
    var companyAddress: String = "",
    var companyPA: String = "",
    var companyBank: String = "",
    var companyBankAddress: String = "",
    var companyBIK: String = "",
    var companyPRN: String = "",
    var companyPhone: String? = null,
    var works: MutableList<OO.Work> = mutableListOf(),
    var dpcs: MutableList<OO.DPC> = mutableListOf(),
    var dfcs: MutableList<OO.DFC> = mutableListOf(),
)
