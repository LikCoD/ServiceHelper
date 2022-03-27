package ldcapps.servicehelper.controllers

import javafx.beans.binding.BooleanExpression
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableRow
import javafx.scene.control.TableView
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Priority
import ldcapps.servicehelper.Name
import ldcapps.servicehelper.styles.MainStyle
import liklibs.db.annotations.Primary
import liklibs.db.utlis.ConflictResolver
import tornadofx.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

class ConflictResolverView private constructor() : View() {
    var conflict = conflicts.first()
    private lateinit var localTable: TableView<Any>
    private lateinit var serverTable: TableView<Any>

    override val root = vbox(10) {
        addClass(MainStyle.mainWindow)

        button("Продолжить") {
            action {
                val items = localTable.selectionModel.selectedItems + serverTable.selectionModel.selectedItems
                (conflict.first as ConflictResolver<Any>).resolve(items)
                conflicts.removeFirst()
                if (conflicts.isEmpty()) {
                    onAllResolved()
                    close()
                    return@action
                }

                conflict = conflicts.first()
                initTable()
            }
        }

        hbox {
            vbox {
                hgrow = Priority.ALWAYS
                serverTable = tableview {
                    selectionModel.selectionMode = SelectionMode.MULTIPLE
                }
                button("Выбрать все") {
                    action {
                        localTable.selectionModel.clearSelection()
                        serverTable.selectionModel.selectAll()
                    }
                }
            }
            vbox {
                hgrow = Priority.ALWAYS
                localTable = tableview {
                    selectionModel.selectionMode = SelectionMode.MULTIPLE
                }
                button("Выбрать все") {
                    action {
                        serverTable.selectionModel.clearSelection()
                        localTable.selectionModel.selectAll()
                    }
                }
            }

            serverTable.bindSelection(localTable)
            localTable.bindSelection(serverTable)

            initTable()
        }
    }

    override fun onBeforeShow() {
        super.onBeforeShow()
        currentStage!!.setOnCloseRequest { it.consume() }
    }

    private fun initTable() {
        serverTable.columns.clear()
        localTable.columns.clear()

        serverTable.items = conflict.second.map { it.server }.asObservable()
        localTable.items = conflict.second.map { it.local }.asObservable()

        serverTable.initColumns()
        localTable.initColumns()

        localTable.selectionModel.selectAll()
    }

    private fun TableView<Any>.bindSelection(table: TableView<Any>) {
        style = "-fx-selection-bar: #007eff; -fx-selection-bar-non-focused: #007eff;"

        addEventFilter(KeyEvent.ANY) { it.consume() }
        setRowFactory {
            val row: TableRow<Any> = TableRow()

            row.addEventFilter(MouseEvent.MOUSE_PRESSED) {
                if (row.isEmpty) return@addEventFilter

                if (selectionModel.selectedItems.contains(row.item)) {
                    selectionModel.clearSelection(row.index)
                    table.selectionModel.select(table.items[items.indexOf(row.item)])
                } else {
                    selectionModel.select(row.item)
                    table.selectionModel.clearSelection(row.index)
                }

                it.consume()
            }

            row
        }
    }

    private fun TableView<Any>.initColumns() {
        val item = this.items.firstOrNull() ?: return

        item::class.declaredMemberProperties.forEach {
            if (it.hasAnnotation<Primary>()) return@forEach
            readonlyColumn(it.findAnnotation<Name>()?.name ?: it.name, it as KProperty1<Any, *>){
                isSortable = false
            }
        }
    }

    companion object {
        private val conflicts: MutableList<Pair<ConflictResolver<*>, List<ConflictResolver.Conflict<*>>>> =
            mutableListOf()
        var onAllResolved: () -> Unit = {}
            set(value) {
                if (conflicts.isEmpty()) value()
                field = value
            }

        fun addConflict(resolver: ConflictResolver<*>, conflict: List<ConflictResolver.Conflict<*>>) {
            if (conflict.isEmpty()) {
                resolver.resolve(emptyList())
                return
            }

            conflicts.add(resolver to conflict)
        }

        fun open() {
            if (conflicts.isEmpty()) return

            ConflictResolverView().openWindow(escapeClosesWindow = false)
        }
    }
}