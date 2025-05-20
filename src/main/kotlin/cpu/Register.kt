package com.tomassirio.cpu

sealed class Register<T>(val index: Short? = null, private var value: T) {
    class ByteRegister : Register<UByte> {
        constructor(index: Short, value: UByte = 0x00u) : super(index, value)  // For V0-VF
        constructor(value: UByte = 0x00u) : super(value = value)  // For DT, ST
    }

    class ShortRegister(value: UShort = 0x0000u) : Register<UShort>(value = value) // For PC, SP, I

    class TimerRegister(value: UByte = 0x00u): Register<UByte>(value = value), Timer { // For DT, ST
        override fun tick() {
            val current = read()
            if (current > 0u) {
                write((current - 1u).toUByte())
            }
        }
    }

    fun write(value: T) {
        this.value = value
    }

    fun read(): T {
        return value
    }
}