package com.tomassirio.system.register

sealed class Register<T>(val index: Short? = null, private var value: T) {
    class ByteRegister(index: Short, value: UByte = 0x00u) : Register<UByte>(index, value) // For V0-VF

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