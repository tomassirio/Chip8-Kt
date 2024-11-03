package com.tomassirio.memory

class Memory(val size: Int, val startAddress: Int = 0x200) {

    @OptIn(ExperimentalUnsignedTypes::class)
    val memory = UByteArray(size)
    var isLocked = false

    @OptIn(ExperimentalUnsignedTypes::class)
    inline fun <reified T> read(address: Int): T {
        validateAddressRange(address)
        return when (T::class) {
            UByte::class -> memory[address] as T
            UShort::class -> {
                val highByte = memory[address].toUShort()
                val lowByte = memory[address + 1].toUShort()
                ((highByte.toUInt() shl 8) or lowByte.toUInt()).toUShort() as T
            }
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    inline fun <reified T> write(address: Int, value: T) {
        validateAddressRange(address)
        if (address < startAddress && isLocked) {
            throw IllegalStateException("Illegal access to Pre Booted Memory. Write from 0x200 onwards")
        }
        when (T::class) {
            UByte::class -> memory[address] = value as UByte
            UShort::class -> {
                val ushortValue = value as UShort
                memory[address] = (ushortValue.toUInt() shr 8).toUByte()
                memory[address + 1] = ushortValue.toUByte()
            }
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    fun lockBootSection() {
        isLocked = true
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    override fun toString(): String {
        val builder = StringBuilder()

        val formatRow: (List<Int>) -> Unit = { range ->
            range.forEach { index ->
                builder.append("0x${index.toString(16).padStart(4, '0')}: 0x${memory[index].toString(16).padStart(4, '0')} ")
            }
            builder.append("\n")
        }

        val chunkSize = 14

        // Print memory before startAddress
        builder.append("Memory before startAddress (0x${startAddress.toString(16)}):\n")
        (0 until startAddress).chunked(chunkSize).forEach { formatRow(it) } // Group in rows of 8 values

        builder.append("\nMemory from startAddress onwards:\n")
        (startAddress until memory.size).chunked(chunkSize).forEach { formatRow(it) } // Group in rows of 8 values

        return builder.toString()
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun Memory.validateAddressRange(address: Int) {
        if (address < 0 || address >= memory.size) {
            throw ArrayIndexOutOfBoundsException("Address $address is out of bounds for memory size ${memory.size}")
        }
    }
}
