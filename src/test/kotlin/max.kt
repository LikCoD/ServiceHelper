import ldcapps.servicehelper.arrFromJSON
import ldcapps.servicehelper.db.DataClasses

var took = 142

fun main() {
    println(arrFromJSON<DataClasses.Report>(".report"))
    val total = arrFromJSON<DataClasses.Report>(".report").filter { it.user == "Даниил" }.sumOf { it.totalWorkPrice }

    println(total)

    println(total * 0.05 - took)
    println(total * 0.05)
}
