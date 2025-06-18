package com.tomassirio.emulation.chip8.ui.pane

import com.tomassirio.emulation.chip8.system.constants.KEYBOARD_KEYS
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.Separator
import javafx.scene.control.TextField
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority

fun createDebugPane(debugFields: MutableMap<String, TextField>): ScrollPane {
    val grid = addGrid()

    val cpuStateRow = addCpuStateColumn(grid, debugFields,0)

    addSeparator(grid, 0, cpuStateRow, Orientation.HORIZONTAL)

    addKeyboardStateColumn(grid, debugFields,cpuStateRow + 1, 0)

    // Separator between CPU state and Register
    addSeparator(grid, 2, 16, Orientation.VERTICAL)

    addRegistersColumn(grid, debugFields,3)
    addStackColumn(grid, debugFields,6)

    val debugPane = ScrollPane(grid).apply {
        isFitToWidth = true
        isFitToHeight = true
        prefViewportWidth = 600.0
        prefViewportHeight = 750.0
    }

    return debugPane
}


private fun addKeyboardStateColumn(grid: GridPane, debugFields: MutableMap<String, TextField>, rowIndex: Int, column: Int) {
    var row = rowIndex
    val keyboardLabel = Label("Keyboard")
    grid.add(keyboardLabel, column, row++)

    KEYBOARD_KEYS.forEach { key ->
        addField(grid, debugFields, "Key[$key]", row++, column)
    }
}

private fun addStackColumn(grid: GridPane, debugFields: MutableMap<String, TextField>, stackCol: Int) {
    // Stack display
    val stackLabel = Label("Stack")
    grid.add(stackLabel, stackCol, 0)

    var row = 1
    for (i in 0..15) {
        addField(grid, debugFields, "Stack[$i]", row++, stackCol)
    }
}

private fun addRegistersColumn(grid: GridPane, debugFields: MutableMap<String, TextField>, colIndex: Int) {
    // Right column: Registers V[0] to V[15]
    val registersLabel = Label("Registers")
    grid.add(registersLabel, colIndex, 0)

    var row = 1
    for (i in 0..15) {
        addField(grid, debugFields,"V[$i]", row++, colIndex)
    }

    // Separator between Registers and Stack
    addSeparator(grid, colIndex + 2, colIndex, Orientation.VERTICAL)
}

private fun addCpuStateColumn(grid: GridPane, debugFields: MutableMap<String, TextField>, colIndex: Int): Int {
    // Left column: CPU state
    var cpuStateRow = 0
    addField(grid, debugFields,"PC", cpuStateRow++, colIndex)
    addField(grid, debugFields,"SP", cpuStateRow++, colIndex)
    addField(grid, debugFields,"I", cpuStateRow++, colIndex)
    addField(grid, debugFields,"DT", cpuStateRow++, colIndex)
    addField(grid, debugFields,"ST", cpuStateRow++, colIndex)
    addField(grid, debugFields,"WaitingForKey", cpuStateRow++, colIndex)
    addField(grid, debugFields,"RegisterToStoreKeyIn", cpuStateRow++, colIndex)

    return cpuStateRow
}

private fun addGrid(): GridPane {
    val grid = GridPane().apply {
        hgap = 10.0
        vgap = 5.0
        padding = Insets(10.0)
    }

    // Column constraints for autosizing
    val labelCol = ColumnConstraints().apply {
        minWidth = 90.0
        prefWidth = 120.0
        isFillWidth = true
        hgrow = Priority.SOMETIMES
    }
    val valueCol = ColumnConstraints().apply {
        minWidth = 60.0
        prefWidth = 100.0
    }

    grid.columnConstraints.addAll(
        labelCol, valueCol, // Left side
        ColumnConstraints().apply { prefWidth = 10.0 }, // Vertical separator spacing
        labelCol, valueCol, // Registers
        ColumnConstraints().apply { prefWidth = 10.0 }, // Another separator
        labelCol, valueCol  // Stack
    )

    return grid
}

private fun addSeparator(grid: GridPane, column: Int, row: Int, orientation: Orientation) {
    grid.add(Separator().apply {
        this.orientation = orientation
    }, column, 0, 1, maxOf(row, 16))
}

private fun addField(grid: GridPane, debugFields: MutableMap<String, TextField>, labelText: String, row: Int, column: Int) {
    val label = Label("$labelText:").apply {
        isWrapText = false
        maxWidth = Double.MAX_VALUE
    }

    val valueField = TextField().apply {
        isEditable = false
        prefColumnCount = 6
        style = "-fx-font-family: 'monospaced';"
    }

    debugFields[labelText] = valueField
    grid.add(label, column, row)
    grid.add(valueField, column + 1, row)
}
