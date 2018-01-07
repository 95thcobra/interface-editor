package com.greg.ui.action

import com.greg.ui.action.change.Change

class Action(change: Change? = null) {
    private val changes = mutableListOf<Change>()

    init {
        if(change != null)
            add(change)
    }

    fun add(change: Change) {
        changes.add(change)
    }

    fun isNotEmpty(): Boolean {
        return changes.isNotEmpty()
    }

    fun size(): Int {
        return changes.size
    }

    fun getChanges(): MutableList<Change> {
        return changes
    }
}