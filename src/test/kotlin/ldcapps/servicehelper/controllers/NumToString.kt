package ldcapps.servicehelper.controllers

import ldcapps.servicehelper.numToStr
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class NumToString{
    @Test
    fun test(){
        assertEquals(numToStr(12.0), "Двенадцать рублей 00 копеек")
        assertEquals(numToStr(12.1), "Двенадцать рублей 10 копеек")
        assertEquals(numToStr(1123122.2), "Один миллион сто двадцать три тысячи сто двадцать два рублей 20 копеек")
        assertEquals(numToStr(-12.0), "Минус двенадцать рублей 00 копеек")
        assertEquals(numToStr(0.45), "Ноль рублей 45 копеек")
        assertEquals(numToStr(0.065), "Ноль рублей 07 копеек")
    }
}