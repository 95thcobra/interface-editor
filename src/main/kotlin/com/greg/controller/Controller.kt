package com.greg.controller

import com.greg.App
import com.greg.canvas.WidgetCanvas
import com.greg.canvas.widget.WidgetRectangle
import com.greg.canvas.widget.WidgetText
import javafx.animation.PauseTransition
import javafx.application.Platform
import javafx.concurrent.Task
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.geometry.VPos
import javafx.scene.Group
import javafx.scene.control.ProgressIndicator
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.util.Duration
import java.net.URL
import java.util.*

class Controller : Initializable {


    /**
     * Ideas
     *
     * On double click on a widget deselect all but that ( and black out rest of canvas? ) display stretching/rotation (photoshop crop like) - enter to finish/ esc to cancel
     *
     * Widget x/y which get's the widget location relative to the canvas: will be needed for saving. Might need canvas as a parameter
     *
     * Expand marquee out to it's own class with inheritance, so you can switch out marquee type/shape etc..
     *
     * Properties panel add different stuff for selection type, edit multiple selections if all the same class
     *
     * Move selection using arrow keys by 1px
     *
     * Round x/y to 1px (or is it already done cause mouse pos can't be .5 of a pixel?
     *
     */
    private var mouseX = 0.0

    private var mouseY = 0.0

    private var click = 0

    private var offsetX = 0.0

    private var offsetY = 0.0

    @FXML
    lateinit var widgetCanvas: Pane

    @FXML
    lateinit var progressIndicator: ProgressIndicator

    lateinit var selected: Group

    private lateinit var canvas: WidgetCanvas

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        canvas = WidgetCanvas(widgetCanvas)

        /*RubberBandSelection(widgetCanvas, selectionModel)

        var text = WidgetText("Here is some text", Color.WHITE)
        text.textOrigin = VPos.TOP
        widgetCanvas.children.add(text)
        text.setStroke(Color.WHITE)
        var rectangle = WidgetRectangle(50.5, 50.5, 10.0, 10.0)
        rectangle.setStroke(Color.WHITE)
        widgetCanvas.children.add(rectangle)
        var rectangle2 = WidgetRectangle(45.5, 45.5, 10.0, 10.0)
        rectangle2.setStroke(Color.WHITE)
        widgetCanvas.children.add(rectangle2)

        selected = Group()
        widgetCanvas.children.add(selected)
        selected.toFront()*/
    }

    @FXML
    fun createWidget() {
    }

    @FXML
    fun createContainer() {
    }

    @FXML
    fun createRectangle() {
        var bounds = widgetCanvas.layoutBounds
        var rectangle = WidgetRectangle(bounds.width / 2.0, bounds.height / 2.0, 10.0, 10.0)
        rectangle.setStroke(Color.WHITE)
        widgetCanvas.children.add(rectangle)
    }

    @FXML
    fun createText() {
        var text = WidgetText("Text", Color.WHITE)
        text.textOrigin = VPos.TOP
        widgetCanvas.children.add(text)
    }

    @FXML
    fun createSprite() {
    }

    @FXML
    fun createModel() {
    }

    @FXML
    fun createTooltip() {
    }

    @FXML
    fun loadCache() {

        createTask(object : Task<Boolean>() {

            @Throws(Exception::class)
            override fun call(): Boolean? {
                val progress = 100.00

                updateMessage(String.format("%.2f%s", progress, "%"))
                updateProgress(100, 100)
                return true
            }

        })
    }

    private fun createTask(task: Task<*>) {

        progressIndicator.isVisible = true

//        progressIndicator.progressProperty()!!.unbind()
//        progressIndicator.progressProperty().bind(task.progressProperty())

        Thread(task).start()

        task.setOnSucceeded {

            val pause = PauseTransition(Duration.seconds(1.0))

            pause.setOnFinished {
                progressIndicator.isVisible = false
            }

            pause.play()
        }

        task.setOnFailed {

            val pause = PauseTransition(Duration.seconds(1.0))

            pause.setOnFinished {
                progressIndicator.isVisible = false
            }

            pause.play()

        }
    }

    @FXML
    fun minimizeProgram() {
        App.mainStage.isIconified = true
    }

    @FXML
    fun closeProgram() {
        Platform.exit()
    }
}