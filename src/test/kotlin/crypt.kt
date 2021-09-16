import java.io.File
import java.security.Key
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


fun main() {
    val crypt = Crypt()
    val token = "DBData.db.getUserUsingLogin<DBData.User>()"
    crypt.encrypt(token)

    println(token)
    println(crypt.decrypt())
}


class Crypt {
    private val cipher = Cipher.getInstance("AES")!!
    private val key: Key

    fun encrypt(data: String) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val cipherText = cipher.doFinal(data.toByteArray(charset("UTF-8")))
            File("token.key").writeBytes(Base64.getEncoder().encode(cipherText))
        } catch (e: Exception) {
        }
    }

    fun decrypt(): String? {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key)
            val cipherText = cipher.doFinal(Base64.getDecoder().decode(File("token.key").readBytes()))
            return String(cipherText)
        } catch (e: Exception) {
        }
        return null
    }

    init {
        key = SecretKeySpec(
            Runtime.getRuntime().exec("wmic baseboard get serialnumber").inputStream.readBytes().copyOf(32),
            "AES"
        )
    }
}