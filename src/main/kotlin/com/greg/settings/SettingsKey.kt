package com.greg.settings

import javafx.scene.input.KeyCode
import javafx.scene.paint.Color

enum class SettingsKey(var default: Any) {
    CANCEL_ON_DEFOCUS(false),
    ACCEPT_KEY_CODE(KeyCode.ENTER.ordinal),
    CANCEL_KEY_CODE(KeyCode.ESCAPE.ordinal),
    SELECTION_STROKE_COLOUR(Color.RED.toString()),
    DEFAULT_STROKE_COLOUR(Color.WHITE.toString()),
    ;

    val key: String = this.name
}