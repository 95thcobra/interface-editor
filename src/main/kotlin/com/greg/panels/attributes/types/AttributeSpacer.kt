package com.greg.panels.attributes.types

import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority

class AttributeSpacer : Pane() {
    init {
        HBox.setHgrow(this, Priority.ALWAYS)
        setMinSize(10.0, 1.0)
    }
}