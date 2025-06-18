package com.tomassirio.emulation.chip8.system.memory.accessor

import com.tomassirio.emulation.chip8.system.memory.Memory
import com.tomassirio.emulation.chip8.system.memory.util.toMemoryBytes

@OptIn(ExperimentalUnsignedTypes::class)
object MemoryAccessor {
    fun readUShort(memory: Memory, address: Int): UShort {
        val mem = memory.getRawMemory()
        return ((mem[address].toUInt() shl 8) or mem[address + 1].toUInt()).toUShort()
    }

    fun writeUShort(memory: Memory, address: Int, value: UShort) {
        val bytes = value.toMemoryBytes()
        memory.writeByte(address, bytes[0])
        memory.writeByte(address + 1, bytes[1])
    }
}