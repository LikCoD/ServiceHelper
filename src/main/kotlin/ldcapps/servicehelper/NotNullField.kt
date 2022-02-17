package ldcapps.servicehelper

import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.control.ComboBox
import javafx.scene.control.DatePicker
import javafx.scene.control.TextField
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.isAccessible

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class NotNullField(val type: String = "", val size: Int = -1) {
    companion object {
        fun <T : Any> get(obj: T, type: String = "") = obj::class.declaredMemberProperties.filter {
            it.isAccessible = true

            val annotation = it.findAnnotation<NotNullField>()

            annotation != null && annotation.type == type
        }

        fun <T : Any> check(obj: T, anim: Boolean = true, type: String = "", checkSize: Boolean): Boolean {
            val fields = get(obj, type)
            var isNotNull = true

            fields.forEach {
                var stringValue: String? = null
                val node = it.getter.call(obj)

                when (node) {
                    is TextField -> stringValue = node.text?.trim()
                    is DatePicker -> stringValue = node.value?.toString()?.trim()
                    is ComboBox<*> -> stringValue = node.value?.toString()?.trim()
                }

                val size = it.findAnnotation<NotNullField>()?.size ?: 0
                if (stringValue == null || stringValue == "" || (checkSize && size > 0 && stringValue.length != size)) {
                    isNotNull = false
                    if (anim) Animations.emptyNode(node as Node)
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