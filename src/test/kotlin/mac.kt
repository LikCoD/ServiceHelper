import java.io.IOException


fun main() {
    try {
        val arr = mutableListOf("1", "2")
        println(arr is List<*>)

        println(Runtime.getRuntime().exec("wmic baseboard get serialnumber").inputStream.reader().readText())
    } catch (ex: IOException) {
        ex.printStackTrace()
    }
}