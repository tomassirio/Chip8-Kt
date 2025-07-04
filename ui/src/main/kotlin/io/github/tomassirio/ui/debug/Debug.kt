package io.github.tomassirio.ui.debug

import io.github.tomassirio.controller.SystemController
import io.github.tomassirio.system.constants.KEYBOARD_KEYS
import io.github.tomassirio.system.debug.DebugLevel
import io.github.tomassirio.system.memory.accessor.MemoryAccessor
import io.github.tomassirio.ui.MemoryRow
import javafx.collections.ObservableList
import javafx.scene.control.TableView
import javafx.scene.control.TextField

class Debugger(
    private val debugFields: MutableMap<String, TextField>,
    private val memoryTable: TableView<MemoryRow>,
    private val memoryItems: ObservableList<MemoryRow>?,
    private val systemController: SystemController
) {
    @OptIn(ExperimentalStdlibApi::class)
    fun debug(debugLevel: DebugLevel) {
        debugFields["PC"]?.text = systemController.debugger.pc.read().toHexString()
        debugFields["SP"]?.text = systemController.debugger.stack.lastIndex.toString()
        debugFields["I"]?.text = systemController.debugger.I.read().toHexString()
        debugFields["DT"]?.text = systemController.debugger.DT.read().toString()
        debugFields["ST"]?.text = systemController.debugger.ST.read().toString()
        debugFields["WaitingForKey"]?.text = systemController.debugger.keyboardState.isWaitingForKey.toString()
        debugFields["RegisterToStoreKeyIn"]?.text =
            systemController.debugger.keyboardState.registerToStoreKeyIn.toString()

        KEYBOARD_KEYS.forEach { key ->
            debugFields["Key[$key]"]?.text = systemController.debugger.keyboardState.keyboardState[key].toString()
        }

        systemController.debugger.registers.forEachIndexed { i, reg ->
            debugFields["V[$i]"]?.text = reg.read().toHexString()
        }

        systemController.debugger.stack.forEachIndexed { i, stackPosition ->
            debugFields["Stack[$i]"]?.text = stackPosition.toHexString()
        }
        if (debugLevel == DebugLevel.FULL) {
            updateMemoryView(memoryTable, memoryItems, systemController)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun updateMemoryView(
        memoryTable: TableView<MemoryRow>,
        memoryItems: ObservableList<MemoryRow>?,
        systemController: SystemController
    ) {
        val memory = systemController.debugger.memory
        val pc = systemController.debugger.pc.read()

        for (i in 0 until memory.size - 1) {
            val newValue = (MemoryAccessor.readUShort(memory, i)).toHexString()
            memoryItems?.set(i, MemoryRow("0x%04X".format(i), newValue))
        }

        memoryTable.items = memoryItems

        // Highlight the current PC row
        memoryTable.selectionModel.clearSelection()
        if (pc.toInt() in 0 until memoryItems?.size!!) {
            memoryTable.selectionModel.select(pc.toInt())
            memoryTable.scrollTo(pc.toInt())
        }
    }
}
