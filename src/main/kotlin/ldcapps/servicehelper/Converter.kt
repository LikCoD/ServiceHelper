package ldcapps.servicehelper

import java.math.BigDecimal
import java.math.BigInteger
import java.util.regex.Pattern

open class Converter {
    open fun <I> toString(item: I) = item.toString()
    open fun <I> toInt(item: I) = toString(item).toDoubleOrNull()?.toInt()
    open fun <I> toDouble(item: I) = toString(item).toDoubleOrNull()
    open fun <I> toFloat(item: I) = toString(item).toFloatOrNull()
    open fun <I> toShort(item: I) = toString(item).toShortOrNull()
    open fun <I> toLong(item: I) = toString(item).toLongOrNull()
    open fun <I> toBoolean(item: I) = toString(item).toBoolean()

    open fun <I> toBigDecimal(item: I) = toString(item).toBigDecimalOrNull()
    open fun <I> toBigInteger(item: I) = toString(item).toBigIntegerOrNull()

    open fun <I> toByte(item: I) = toString(item).toByteOrNull()

    open fun <I> toByteArray(item: I) = toString(item).toByteArray()
    open fun <I> toCharArray(item: I) = toString(item).toCharArray()

    open fun <I> toPattern(item: I) = toString(item).toPattern()
    open fun <I> toRegex(item: I) = toString(item).toRegex()

    inline fun <I, reified O> toGenerics(item: I): O? {
        if (item is O) return item

        return when(O::class.simpleName){
            Int::class.simpleName -> toInt(item) as O
            Double::class.simpleName -> toDouble(item) as O
            Float::class.simpleName -> toFloat(item) as O
            Short::class.simpleName -> toShort(item) as O
            Long::class.simpleName -> toLong(item) as O
            Boolean::class.simpleName -> toBoolean(item) as O
            BigDecimal::class.simpleName -> toBigDecimal(item) as O
            BigInteger::class.simpleName -> toBigInteger(item) as O
            Byte::class.simpleName -> toByte(item) as O
            ByteArray::class.simpleName -> toByteArray(item) as O
            CharArray::class.simpleName -> toCharArray(item) as O
            Pattern::class.simpleName -> toPattern(item) as O
            Regex::class.simpleName -> toRegex(item) as O
            else -> return null
        }
    }
}