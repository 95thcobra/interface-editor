package com.greg.model.widgets.memento

import com.greg.model.widgets.WidgetType
import javafx.beans.property.Property

data class Memento(val type: WidgetType, val values: MutableList<MementoValue> = mutableListOf()) {
    override fun toString(): String {
        return "$type $values"
    }

    fun add(property: Property<*>) {
        values.add(MementoValue(property.value.toString()))
    }

    fun addAll(list: MutableList<String>) {
        list.mapTo(values) { MementoValue(it) }
    }
}