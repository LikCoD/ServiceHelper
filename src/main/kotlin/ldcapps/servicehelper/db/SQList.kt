package ldcapps.servicehelper.db

import ldcapps.servicehelper.isOnline
import ldcapps.servicehelper.toJSON

class SQList<E : Any>(
    private val db: MySqlDb?,
    private val tableName: String,
    private val obj: E,
    private val localList: MutableList<E>,
    private val fileName: String,
    val list: MutableList<E> = db?.getTableFromDB(tableName, obj) ?: localList,
) : MutableList<E> by list {

    override fun add(element: E): Boolean {
        return list.add(element).also { addToDB(element) }
    }

    override fun add(index: Int, element: E) {
        list.add(index, element).also { addToDB(element) }
    }

    override fun addAll(elements: Collection<E>): Boolean {
        return list.addAll(elements).also { elements.forEach { addToDB(it) } }
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        return list.addAll(index, elements).also { elements.forEach { addToDB(it) } }
    }

    override fun clear() {
        db?.dbConnection?.prepareStatement("TRUNCATE `${db.dbName}`.`$tableName`")?.execute()
        list.clear()
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        return list.removeAll(elements).also { elements.forEach { removeFromDB(list.indexOf(it)) } }
    }

    override fun removeAt(index: Int): E {
        return list.removeAt(index).also { removeFromDB(index) }
    }

    override fun remove(element: E): Boolean {
        return list.remove(element).also { removeFromDB(list.indexOf(element)) }
    }

    override fun set(index: Int, element: E): E {
        return list.set(index, element).also { changeDB(element, index) }
    }

    private fun addToDB(element: E) {
        if (isOnline) {
            var names = "`id`"
            var fields = "NULL"

            element::class.java.declaredFields.forEach {
                it.isAccessible = true

                if (it.get(element) != null && it.name != "Companion") {
                    names += ", `${it.name}`"
                    fields += ", '${it.get(element)}'"
                }
            }

            db?.dbConnection?.prepareStatement("INSERT INTO `$tableName` ($names) VALUES ($fields)")?.executeUpdate()
        }

        toJSON(fileName, this as MutableList<E>)
    }

    private fun removeFromDB(index: Int) {
        if (isOnline) {
            db?.dbConnection?.prepareStatement("DELETE FROM `$tableName` WHERE `$tableName`.`id` = ${index + 1}")
                ?.execute()

            for (i in index..(db?.getRowCount(tableName) ?: -1))
                db!!.dbConnection.prepareStatement("UPDATE `$tableName` SET `id` = '${i + 1}' WHERE `$tableName`.`id` = ${i + 2}")
                    .execute()

            db?.dbConnection?.prepareStatement("ALTER TABLE `$tableName` AUTO_INCREMENT = ${db.getRowCount(tableName) - 1}")
        }

        toJSON(fileName, this as MutableList<E>)
    }

    private fun changeDB(element: E, index: Int) {
        if (isOnline) {
            var update = ""

            element::class.java.declaredFields.forEach {
                it.isAccessible = true

                update += ", `${it.name}` = '${it.get(element)}'"
            }

            db?.dbConnection?.prepareStatement("UPDATE `$tableName` SET ${update.substring(2)} WHERE `$tableName`.`id` = ${index + 1}")
                ?.execute()
        }

        toJSON(fileName, this as MutableList<E>)
    }
}