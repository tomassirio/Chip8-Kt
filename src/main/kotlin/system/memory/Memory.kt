package com.tomassirio.system.memory

import com.tomassirio.system.memory.util.toMemoryBytes

@OptIn(ExperimentalUnsignedTypes::class)
class Memory(size: Int, val startAddress: Int = 0x200) {

    private val memory = UByteArray(size)
    private var isLocked = false

    fun <T> read(address: Int, converter: (UByteArray, Int) -> T): T {
        validateAddressRange(address)
        return converter(memory, address)
    }

    fun write(address: Int, value: Any) {
        validateAddressRange(address)
        validateMemoryWriting(address)
        val bytes = when (value) {
            is UByte -> value.toMemoryBytes()
            is UShort -> value.toMemoryBytes()
            is UInt -> value.toMemoryBytes()
            else -> throw IllegalArgumentException("Unsupported type")
        }
        for ((offset, byte) in bytes.withIndex()) {
            memory[address + offset] = byte
        }
    }

    fun lockBootSection() {
        isLocked = true
    }

    private fun validateAddressRange(address: Int) {
        if (address < 0 || address >= memory.size) {
            throw ArrayIndexOutOfBoundsException("Address $address is out of bounds for memory size ${memory.size}")
        }
    }

    private fun validateMemoryWriting(address: Int) {
        if (address < startAddress && isLocked) {
            throw IllegalStateException("Illegal access to Pre Booted Memory. Write from 0x200 onwards")
        }
    }
}
