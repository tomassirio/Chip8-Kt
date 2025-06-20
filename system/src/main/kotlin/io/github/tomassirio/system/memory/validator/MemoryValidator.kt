package io.github.tomassirio.system.memory.validator

class MemoryValidator(
    private val memorySize: Int,
    private val startAddress: Int,
    private val isLocked: () -> Boolean
) {
    fun validate(address: Int) {
        require(address in 0 until memorySize) {
            "Address $address out of bounds (0..${memorySize - 1})"
        }
    }

    fun validateWrite(address: Int) {
        validate(address)
        if (isLocked() && address < startAddress) {
            error("Write denied below 0x${startAddress.toString(16)} (boot section locked)")
        }
    }
}