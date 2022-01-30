import kotlinx.serialization.ExperimentalSerializationApi
import ldcapps.servicehelper.controllers.OOController
import ldcapps.servicehelper.fromJSON
import org.jetbrains.annotations.TestOnly
import org.junit.jupiter.api.Test
import java.io.File

@ExperimentalSerializationApi
internal class OONumber {

    @TestOnly
    fun currentOONumber(dir: String, extension: String? = null): Int {
        val fileList = File("P:\\ServiceHelper\\OO\\$dir").listFiles { _, name ->
            if (extension != null) name.endsWith(".$extension") else true
        }

        if (fileList == null || fileList.isEmpty()) return 0

        val numList = fileList.map { fromJSON<OOController.OOAndBill>(it).number }.sorted()

        for (i in 1 until numList.size)
            if (numList[i - 1] + 1 != numList[i]) return numList[i - 1] + 1

        return numList.last() + 1
    }

    @Test
    fun test(){
        println(currentOONumber("Безнал"))
    }
}