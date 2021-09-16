import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ldcapps.servicehelper.db.DataClasses
import ldcapps.servicehelper.db.SQList
import netscape.javascript.JSObject

fun main(){

    //println(toJSON(DataClasses.reports))
    //println(arrToJSON<SQList<*>, DataClasses.Report>((DataClasses.reports)))
}

inline fun <reified E, L> arrToJSON(obj: E): String {
        return Json.encodeToString(obj as List<L>)
}