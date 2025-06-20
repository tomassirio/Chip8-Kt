package io.github.tomassirio.system.memory.util

fun UShort.toMemoryBytes(): List<UByte> =
    listOf((this.toUInt() shr 8).toUByte(), this.toUByte())