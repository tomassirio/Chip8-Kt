package com.tomassirio.memory

fun UByte.toMemoryBytes(): List<UByte> = listOf(this)

fun UShort.toMemoryBytes(): List<UByte> =
    listOf((this.toUInt() shr 8).toUByte(), this.toUByte())

fun UInt.toMemoryBytes(): List<UByte> = when {
    this <= UByte.MAX_VALUE.toUInt() -> listOf(this.toUByte())
    this <= UShort.MAX_VALUE.toUInt() -> {
        val ushortValue = this.toUShort()
        listOf((ushortValue.toUInt() shr 8).toUByte(), ushortValue.toUByte())
    }
    else -> throw IllegalArgumentException("Unsupported UInt value")
}

@OptIn(ExperimentalUnsignedTypes::class)
fun UByteArray.toUByteAt(address: Int): UByte = this[address]

@OptIn(ExperimentalUnsignedTypes::class)
fun UByteArray.toUShortAt(address: Int): UShort {
    val highByte = this[address].toUShort()
    val lowByte = this[address + 1].toUShort()
    return ((highByte.toUInt() shl 8) or lowByte.toUInt()).toUShort()
}