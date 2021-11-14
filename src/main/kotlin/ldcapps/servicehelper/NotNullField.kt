package ldcapps.servicehelper

import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.control.ComboBox
import javafx.scene.control.DatePicker
import javafx.scene.control.TextField

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
annotation class NotNullField(val type: String = "", val size: Int = -1) {
    companion object {
        fun <T : Any> get(obj: T, type: String = "") = obj::class.java.declaredFields.map {
            it.isAccessible = true

            val field = it.get(obj)
            val annotation = it.getAnnotation(NotNullField::class.java)

            field to annotation
        }.filter {
            it.first is Node && it.second != null && it.second.type == type
        }

        fun <T : Any> check(obj: T, anim: Boolean = true, type: String = "", checkSize: Boolean): Boolean {
            val fields = get(obj, type)
            var isNotNull = true

            fields.forEach {
                var stringValue: String? = null

                when (val node = it.first) {
                    is TextField -> stringValue = node.text?.trim()
                    is DatePicker -> stringValue = node.value?.toString()?.trim()
                    is ComboBox<*> -> stringValue = node.value?.toString()?.trim()
                }

                val size = it.second.size
                if (stringValue == null || stringValue == "" || (checkSize && size > 0 && stringValue.length != size)) {
                    isNotNull = false
                    if (anim) Animations.emptyNode(it.first as Node)
                }
            }

            return isNotNull
        }

        fun Initializable.check(anim: Boolean = true, type: String = "", checkSize: Boolean = true) =
            check(this, anim, type, checkSize)

        fun Node.check(anim: Boolean = true, type: String = "", checkSize: Boolean = true) =
            check(this, anim, type, checkSize)
    }
}