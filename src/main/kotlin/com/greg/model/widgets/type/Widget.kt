package com.greg.model.widgets.type

import com.greg.controller.canvas.DragContext
import com.greg.model.settings.Settings
import com.greg.model.widgets.WidgetBuilder
import com.greg.model.widgets.WidgetType
import com.greg.model.widgets.memento.Memento
import com.greg.model.widgets.memento.MementoBuilder
import com.greg.model.widgets.properties.Properties
import com.greg.model.widgets.properties.PropertyValues
import com.greg.model.widgets.properties.extended.BoolProperty
import com.greg.model.widgets.properties.extended.IntProperty
import com.greg.model.widgets.properties.extended.ObjProperty
import javafx.beans.property.Property

open class Widget(builder: WidgetBuilder, id: Int) {

    val type: WidgetType = builder.type
    val identifier = id
    val name: String = type.name.toLowerCase().capitalize()
    val dragContext = DragContext()

    val properties = Properties()

    private var x: IntProperty? = null
    private var y: IntProperty? = null
    private var width: IntProperty? = null
    private var height: IntProperty? = null
    private var widthBounds: ObjProperty<IntRange>? = null
    private var heightBounds: ObjProperty<IntRange>? = null

    private var locked: BoolProperty? = null
    private var selected: BoolProperty? = null
    private var hidden: BoolProperty? = null

    internal var widthToggle: PropertyValues
    internal var heightToggle: PropertyValues

    init {
        properties.add(xProperty(), category = "Layout")
        properties.add(yProperty(), category = "Layout")
        widthToggle = properties.addCapped(widthProperty(), widthBoundsProperty(), "Layout")
        heightToggle = properties.addCapped(heightProperty(), heightBoundsProperty(), "Layout")
        properties.addPanel(lockedProperty(), false)
        properties.addPanel(selectedProperty(), false)
        properties.addPanel(hiddenProperty(), false)
    }

    fun widthBoundsProperty(): ObjProperty<IntRange> {
        if(widthBounds == null)
            widthBounds = ObjProperty(this, "widthBounds", IntRange(Settings.getInt(Settings.DEFAULT_WIDGET_MINIMUM_WIDTH), Settings.getInt(Settings.WIDGET_CANVAS_WIDTH)))

        return widthBounds!!
    }

    fun heightBoundsProperty(): ObjProperty<IntRange> {
        if(heightBounds == null)
            heightBounds = ObjProperty(this, "heightBounds", IntRange(Settings.getInt(Settings.DEFAULT_WIDGET_MINIMUM_HEIGHT), Settings.getInt(Settings.WIDGET_CANVAS_HEIGHT)))

        return heightBounds!!
    }

    fun setLocked(value: Boolean) {
        lockedProperty().set(value)
    }
    fun isLocked(): Boolean {
        return lockedProperty().get()
    }

    fun lockedProperty(): BoolProperty {
        if (locked == null)
            locked = BoolProperty(this, "locked", false)

        return locked!!
    }

    fun isHidden(): Boolean {
        return hiddenProperty().get()
    }

    fun setHidden(value: Boolean) {
        hiddenProperty().set(value)
    }

    fun hiddenProperty(): BoolProperty {
        if (hidden == null)
            hidden = BoolProperty(this, "hidden", false)

        return hidden!!
    }

    fun isSelected(): Boolean {
        return selectedProperty().get()
    }

    fun setSelected(value: Boolean) {
        selectedProperty().set(if (value && isLocked()) false else value)
    }

    fun selectedProperty(): BoolProperty {
        if (selected == null)
            selected = BoolProperty(this, "selected", false)

        return selected!!
    }
    fun getX(): Int {
        return xProperty().get()
    }

    fun setX(value: Int) {
        xProperty().set(value)
    }

    fun xProperty(): IntProperty {
        if (x == null)
            x = IntProperty(this, "x", Settings.getInt(Settings.DEFAULT_POSITION_X))

        return x!!
    }

    fun getY(): Int {
        return yProperty().get()
    }

    fun setY(value: Int) {
        yProperty().set(value)
    }

    fun yProperty(): IntProperty {
        if (y == null)
            y = IntProperty(this, "y", 100)

        return y!!
    }

    fun getWidth(): Int {
        return widthProperty().get()
    }

    fun setWidth(value: Int) {
        widthProperty().set(value)
    }

    fun widthProperty(): IntProperty {
        if (width == null)
            width = IntProperty(this, "width", Settings.getInt(Settings.DEFAULT_RECTANGLE_WIDTH))
        return width!!
    }

    fun getHeight(): Int {
        return heightProperty().get()
    }

    fun setHeight(value: Int) {
        heightProperty().set(value)
    }

    fun heightProperty(): IntProperty {
        if (height == null)
            height = IntProperty(this, "height", Settings.getInt(Settings.DEFAULT_RECTANGLE_HEIGHT))
        return height!!
    }

    fun getMemento(): Memento {
        return MementoBuilder(this).build()
    }

    fun restore(memento: Memento) {
        for ((index, value) in properties.get().withIndex()) {
            if(value.property == selectedProperty())
                continue
            (value.property as Property<*>).value = memento.values[index].convert(value.property)
        }
    }
}