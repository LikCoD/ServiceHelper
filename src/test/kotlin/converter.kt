
import ldcapps.servicehelper.Converter

fun main() {
    val res = Converter().toGenerics<String, CharArray>("153")

    println(res?.forEach { println(it) })
}

