package com.greg.view.canvas.states

import com.greg.controller.canvas.PannableCanvas
import com.greg.controller.selection.MarqueeController
import com.greg.controller.selection.SelectionController
import com.greg.controller.widgets.WidgetsController
import com.greg.view.canvas.CanvasState
import com.greg.view.canvas.CanvasView
import com.greg.view.canvas.widgets.WidgetShape
import javafx.scene.Cursor
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent

class DefaultState(val view: CanvasView, val canvas: PannableCanvas, val widgets: WidgetsController) : CanvasState {

    private var marquee = MarqueeController(widgets, canvas)
    private val selection = SelectionController(widgets)

    /**
     * Default state variables
     */
    private var cloned = false
    private var horizontal = 0
    private var vertical = 0

    override fun handleMousePress(event: MouseEvent) {
        if (!CanvasView.spaceHeld)
            selection.start(event)

        val clone = event.isShiftDown && widgets.hasSelection()
        if (clone) {
            if (!cloned) {
                widgets.clone()
                cloned = true
            }
        }

        //If shift cloned start action with cloned widget
        if (clone)
            widgets.start(widgets.getWidget(getClone(event)))
        else
            widgets.start(widgets.getWidget(event.target))

        if (CanvasView.spaceHeld)
            setCursor(Cursor.CLOSED_HAND)
        else
            marquee.init(event)
    }

    override fun handleMouseDrag(event: MouseEvent) {
        if (event.isPrimaryButtonDown) {
            //Transform marquee box to match mouse position
            if (!CanvasView.spaceHeld)
                marquee.handle(event)
            else
                setCursor(Cursor.CLOSED_HAND)
        }
    }

    override fun handleMouseRelease(event: MouseEvent) {
        cloned = false
        marquee.select(event)

        if (CanvasView.spaceHeld)
            setCursor(Cursor.OPEN_HAND)

        widgets.finish()
    }

    override fun handleDoubleClick(event: MouseEvent) {
        val widget = widgets.getWidget(event.target)
        val shape = widgets.getShape(event.target)
        if (widget != null && shape != null && widget.type.resizable)
            view.editState(widget, shape)

        cloned = false
        widgets.finish()
    }

    override fun handleMouseClick(event: MouseEvent) {
        view.root.requestFocus()
    }

    override fun handleKeyPress(event: KeyEvent) {
        if(event.code == KeyCode.SPACE) {
            marquee.remove(view.root)
            return
        }
        if (event.code != KeyCode.SHIFT)
            widgets.start()

        if (event.code == KeyCode.RIGHT || event.code == KeyCode.LEFT || event.code == KeyCode.UP || event.code == KeyCode.DOWN)
            move(event)
    }

    override fun handleKeyRelease(event: KeyEvent) {
        if (event.code == KeyCode.RIGHT || event.code == KeyCode.LEFT || event.code == KeyCode.UP || event.code == KeyCode.DOWN)
            reset(event.code)
        else if (event.code == KeyCode.DELETE)
            widgets.deleteSelection()
        else if (!event.isShiftDown)
            cloned = false

        if (event.isControlDown) {
            when (event.code) {
                KeyCode.A -> {
                    widgets.selectAll()
                }
                KeyCode.X -> {
                    widgets.cut()
                }
                KeyCode.C -> {
                    widgets.copy()
                }
                KeyCode.V -> {
                    widgets.paste()
                }
                KeyCode.Z -> {
                    if (event.isShiftDown)
                        widgets.redo()
                    else
                        widgets.undo()
                }
                else -> {
                }
            }
        }

        if (event.code != KeyCode.SHIFT)
            widgets.finish()
    }

    override fun onClose() {
        marquee.remove(view.root)
    }

    private fun setCursor(cursor: Cursor) {
        view.root.cursor = cursor
    }

    private fun getClone(event: MouseEvent): WidgetShape? {
        return canvas.children.filterIsInstance<WidgetShape>()
                .firstOrNull { it.boundsInParent.intersects(event.x, event.y, 1.0, 1.0) }
    }

    private fun move(event: KeyEvent) {
        when {
            event.code == KeyCode.RIGHT -> horizontal = 1
            event.code == KeyCode.LEFT -> horizontal = -1
            event.code == KeyCode.UP -> vertical = -1
            event.code == KeyCode.DOWN -> vertical = 1
        }

        move(if (event.isShiftDown) horizontal * 10 else horizontal, if (event.isShiftDown) vertical * 10 else vertical)
    }

    private fun move(x: Int, y: Int) {
        widgets.forSelected { widget ->
            widget.setX(widget.getX() + x)
            widget.setY(widget.getY() + y)
        }
    }

    private fun reset(code: KeyCode) {
        if (code == KeyCode.RIGHT || code == KeyCode.LEFT)
            horizontal = 0

        if (code == KeyCode.UP || code == KeyCode.DOWN)
            vertical = 0
    }
}