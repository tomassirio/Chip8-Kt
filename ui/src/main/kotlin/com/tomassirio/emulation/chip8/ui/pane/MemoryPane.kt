package com.tomassirio.emulation.chip8.ui.pane

import com.tomassirio.emulation.chip8.ui.MemoryRow
import javafx.collections.ObservableList
import javafx.scene.control.ScrollPane
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory

fun createMemoryPane(
    memoryTable: TableView<MemoryRow>,
    memoryItems: ObservableList<MemoryRow>?
): ScrollPane {
    val addressCol = TableColumn<MemoryRow, String>("Address").apply {
        cellValueFactory = PropertyValueFactory("address")
        prefWidth = 80.0
    }

    val valueCol = TableColumn<MemoryRow, String>("Value").apply {
        cellValueFactory = PropertyValueFactory("value")
        prefWidth = 80.0
    }

    memoryTable.columns.addAll(addressCol, valueCol)
    memoryTable.items = memoryItems
    memoryTable.selectionModel.isCellSelectionEnabled = false
    memoryTable.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS

    val memoryPane = ScrollPane(memoryTable)
    memoryPane.prefWidth = 200.0
    memoryPane.isFitToWidth = true
    memoryPane.isFitToHeight = true

    return memoryPane
}
