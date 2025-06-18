package com.tomassirio.emulation.chip8.controller.loader

import com.tomassirio.emulation.chip8.system.memory.Memory

class RomLoader(private val memory: Memory) {

    fun loadRom(data: ByteArray) {
        data.forEachIndexed { index, byte ->
            memory.writeByte(
                memory.startAddress + index,
                byte.toUByte()
            )
        }
    }
}
