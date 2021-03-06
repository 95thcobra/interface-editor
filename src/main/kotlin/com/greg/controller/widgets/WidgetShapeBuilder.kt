package com.greg.controller.widgets

import com.greg.model.widgets.WidgetType
import com.greg.model.widgets.type.Widget
import com.greg.view.canvas.widgets.*

class WidgetShapeBuilder(val widget: Widget) {
    fun build(): WidgetShape {
        return when(widget.type) {
            WidgetType.TEXT -> TextShape(widget.identifier, widget.getWidth(), widget.getHeight())
            WidgetType.RECTANGLE -> RectangleShape(widget.identifier, widget.getWidth(), widget.getHeight())
            WidgetType.SPRITE -> SpriteShape(widget.identifier, widget.getWidth(), widget.getHeight())
            WidgetType.CACHE_SPRITE -> CacheSpriteShape(widget.identifier, widget.getWidth(), widget.getHeight())
            else -> {
                WidgetShape(widget.identifier, widget.getWidth(), widget.getHeight())
            }
        }
    }
}