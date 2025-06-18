package com.tomassirio.emulation.chip8.system.memory.factory

import com.tomassirio.emulation.chip8.system.constants.FONTSET_BASE_ADDRESS
import com.tomassirio.emulation.chip8.system.memory.Memory
import com.tomassirio.emulation.chip8.system.memory.preload.FontSet

object MemoryFactory {
    private const val MEMORY_SIZE = 4096

    fun createMemory(): Memory = initMemory(MEMORY_SIZE)

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