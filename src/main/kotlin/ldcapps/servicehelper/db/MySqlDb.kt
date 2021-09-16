package ldcapps.servicehelper.db

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ldcapps.servicehelper.crypt
import ldcapps.servicehelper.generateToken
import ldcapps.servicehelper.getToken
import ldcapps.servicehelper.isOnline
import java.io.File
import java.sql.Connection
import java.sql.Date
import java.sql.DriverManager
import java.sql.ResultSet

class MySqlDb(val dbName: String) {
    companion object {
        private var host: String = ""
        private var port: Int = 0
        private var login: String = ""
        private var password: String = ""
        var token = getToken()

        fun set(host: String = "", port: Int = 0, login: String = "", password: String = "") {
            this.host = host
            this.port = port
            this.login = login
            this.password = password
        }
    }

    init {
        Class.forName("com.mysql.cj.jdbc.Driver")
    }

    val dbConnection: Connection = DriverManager.getConnection("jdbc:mysql://$host:$port/$dbName", login, password)

    fun getRowCount(tableName: String): Int {
        val resultSet = dbConnection.prepareStatement("select count(*) from $tableName").executeQuery()
        resultSet.next()
        return resultSet.getInt(1)
    }

    fun <E : Any> getTableFromDB(tableName: String, obj: E): MutableList<E>? {
        try {
            if (!isOnline) return null

            val res = dbConnection.createStatement().executeQuery("SELECT * FROM `$tableName` ORDER BY `id` DESC")
            val list = mutableListOf<E>()

            while (res.next()) {
                list.add(fill(res, obj))
            }

            return list
        } catch (e: ExceptionInInitializerError) {
        }
        return null
    }

    fun <E : Any> getUserUsingLogin(login: String, password: String, obj: E): E? {
        val query =
            dbConnection.prepareStatement("SELECT * FROM `users` WHERE `login`='$login' AND `password`='$password'")
                .executeQuery()
        return if (query != null && query.next()) {
            token = crypt.encrypt(query.getString("token"))

            File("token.key").writeText(token!!)

            fill(query, obj)
        } else null
    }

    fun <E : Any> getUser(obj: E): E? {
        try {
            if (!isOnline) return null

            val query = dbConnection.prepareStatement("SELECT * FROM `users` WHERE `token`='$token'").executeQuery()

            return if (query != null && query.next()) fill(query, obj)
            else null
        } catch (e: ExceptionInInitializerError) {
        }
        return null
    }

    fun <E> saveUser(user: E, login: String? = null, password: String? = null): Boolean {
        try {
            var set = if (login != null) "`login` = '$login', `password` = '$password'" else ""

            user!!::class.java.declaredFields.forEach {
                it.isAccessible = true

                if (it.get(user) != null)
                    set += ", `${it.name}` = '${it.get(user)}'"
            }

            if (login == null) set = set.substring(2)

            dbConnection.prepareStatement("UPDATE `users` SET $set WHERE `users`.`token` = '$token'").executeUpdate()

            return true
        } catch (e: ExceptionInInitializerError) {
        }
        return false
    }

    fun deleteUser(): Boolean = try {
        dbConnection.prepareStatement("DELETE FROM `users` WHERE `token` = '$token'").executeUpdate()
        true
    } catch (e: ExceptionInInitializerError) {
        false
    }

    fun checkPassword(password: String): Boolean = try {
        dbConnection.prepareStatement("SELECT `password` FROM `users` WHERE `token`='$token'").executeQuery()
            .apply { next() }.getString("password") == password
    } catch (ex: Exception) {
        false
    }

    fun checkToken(): Boolean =
        dbConnection.prepareStatement("SELECT `password` FROM `users` WHERE `token`='$token'").executeQuery().next()

    fun <E> addUser(user: E, login: String, password: String): Status {
        try {
            var token: String

            do token = generateToken()
            while (
                dbConnection.prepareStatement("SELECT * FROM `users` WHERE `token`='$token'").executeQuery().next()
            )

            if (dbConnection.prepareStatement("SELECT * FROM `users` WHERE `login`='$login'").executeQuery().next())
                return Status.USER_EXIST

            var names = "`id`, `token`, `login`, `password`"
            var fields = "NULL, '$token', '$login', '$password'"

            try {

                user!!::class.java.declaredFields.forEach {
                    it.isAccessible = true

                    if (it.get(user) != null) {
                        names += ", `${it.name}`"
                        fields += ", '${it.get(user)}'"
                    }
                }

            } catch (ex: Exception) {
                return Status.DATA_ERROR
            }

            dbConnection.prepareStatement("INSERT INTO `users` ($names) VALUES ($fields)").executeUpdate()
            return Status.NORMAL
        } catch (ex: Exception) {
            ex.printStackTrace()
            return Status.CONNECTION_ERROR
        }
    }

    fun <E : Any> fill(res: ResultSet, obj: E): E {
        val newObj = obj::class.java.getDeclaredConstructor().newInstance()
        newObj::class.java.declaredFields.forEach {
            if (it.name != "Companion")
            try {
                it.isAccessible = true

                var o = res.getObject(it.name)

                if (o is Date)
                     o = DataClasses.Date(o)

                if (o?.toString()?.firstOrNull() == '[' && o.toString().lastOrNull() == ']')
                    it.set(newObj, Json.decodeFromString<Array<String>>(o.toString()).toMutableList())
                else it.set(newObj, o)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        return newObj
    }

    enum class Status(val text: String) {
        NORMAL(""),
        USER_EXIST("Пользователь с таким логином уже существует"),
        CONNECTION_ERROR("Проверьте подключение к интернету"),
        DATA_ERROR("Проверьте правильномть данннех")
    }
}