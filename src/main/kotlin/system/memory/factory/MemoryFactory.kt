package com.tomassirio.system.memory.factory

import com.tomassirio.system.constants.FONTSET_BASE_ADDRESS
import com.tomassirio.system.memory.Memory
import com.tomassirio.system.memory.preload.FontSet

object MemoryFactory {
    private const val MEMORY_SIZE = 4096
    private const val ETI_START = 0x600

    fun createMemory(): Memory = initMemory(MEMORY_SIZE)

    fun createETIMemory(): Memory = initMemory(MEMORY_SIZE, ETI_START)

    private fun initMemory(size: Int, startAddress: Int = 0x200): Memory =
        Memory(size, startAddress).apply {
            loadFontSet(this)
            lockBootSection()
        }

    private fun loadFontSet(memory: Memory) {
        val fontBase = FONTSET_BASE_ADDRESS
        val fontData = FontSet().getFontSet()
            .toSortedMap()
            .values
            .flatten()

        fontData.forEachIndexed { index, byte ->
            memory.writeByte((fontBase + index.toUInt()).toInt(), byte)
        }
    }
}