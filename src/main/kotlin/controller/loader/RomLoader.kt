package com.tomassirio.controller.loader

import com.tomassirio.system.memory.Memory

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
