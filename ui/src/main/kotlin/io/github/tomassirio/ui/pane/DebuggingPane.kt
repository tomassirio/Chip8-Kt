package io.github.tomassirio.ui.pane

import io.github.tomassirio.controller.SystemController
import io.github.tomassirio.system.debug.DebugLevel
import io.github.tomassirio.ui.MemoryRow
import javafx.collections.ObservableList
import javafx.scene.Scene
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.stage.Stage

fun createDebuggingSplitPane(
    stage: Stage, debugFields: MutableMap<String, TextField>,
    memoryTable: TableView<MemoryRow>,
    memoryItems: ObservableList<MemoryRow>,
    systemController: SystemController,
    debugLevel: DebugLevel
) {
    val rightPane: HBox
    val cpuStatePane = createDebugPane(debugFields)

    when(debugLevel) {
        DebugLevel.NONE -> return
        DebugLevel.MINIMAL -> rightPane = HBox(cpuStatePane)
        DebugLevel.FULL -> {
            val memoryPane = createMemoryPane(memoryTable, memoryItems)
            rightPane = HBox(cpuStatePane, memoryPane)
        }

    }

    rightPane.spacing = 10.0

    val scene = Scene(rightPane)
    val debugStage = Stage()
    debugStage.scene = scene
    debugStage.title = "Debug Window"
    debugStage.x = stage.x + stage.width + 20
    debugStage.y = stage.y
    debugStage.show()

    if (debugLevel == DebugLevel.FULL) {
        initializeMemoryTable(systemController, memoryItems)
    }
}

@OptIn(ExperimentalStdlibApi::class)
private fun initializeMemoryTable(systemController: SystemController, memoryItems: ObservableList<MemoryRow>) {
    val memory = systemController.debugger.memory
    memoryItems.clear()
    for (i in 0 until memory.size) {
        val address = "0x%04X".format(i)
        val value = (memory.readByte(i)).toHexString()
        memoryItems.add(MemoryRow(address, value))
    }
}

