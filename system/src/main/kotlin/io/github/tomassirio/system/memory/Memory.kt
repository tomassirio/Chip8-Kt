package io.github.tomassirio.system.memory

import io.github.tomassirio.system.memory.validator.MemoryValidator

@OptIn(ExperimentalUnsignedTypes::class)
class Memory(val size: Int, val startAddress: Int = 0x200) {
    private val memory = UByteArray(size)
    private var isLocked = false
    private val validator = MemoryValidator(size, startAddress, ::isLocked)

    fun readByte(address: Int): UByte {
        validator.validate(address)
        return memory[address]
    }

    fun writeByte(address: Int, value: UByte) {
        validator.validateWrite(address)
        memory[address] = value
    }

    fun lockBootSection() {
        isLocked = true
    }

    internal fun getRawMemory(): UByteArray = memory
}
