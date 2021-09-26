import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.fromJSON
import org.jetbrains.annotations.TestOnly
import org.junit.jupiter.api.Test
import java.io.File

internal class OONumber {

    @TestOnly
    fun currentOONumber(dir: String, extension: String? = null): Int {
        val fileList = File("P:\\ServiceHelper\\OO\\$dir").listFiles { _, name ->
            if (extension != null) name.endsWith(".$extension") else true
        }

        if (fileList == null || fileList.isEmpty()) return 0

        val numList = fileList.map { fromJSON<OOAndBill>(it).number }.sorted()

        for (i in 1 until numList.size)
            if (numList[i - 1] + 1 != numList[i]) return numList[i - 1] + 1

        return numList.last() + 1
    }

    @Test
    fun test(){
        println(currentOONumber("Безнал"))
    }

    class OOAndBill(
        var number: Int = 0,
        var registrationDate: DataClasses.Date = DataClasses.Date(),
        var executionDate: DataClasses.Date = DataClasses.Date(),
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
        var companyPhone: String? = null
    )
}