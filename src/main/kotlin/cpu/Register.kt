package com.tomassirio.cpu

sealed class Register<T>(val name: String, private var value: T) {
    class ByteRegister(name: String, value: UByte = 0x00u) : Register<UByte>(name, value)
    class ShortRegister(name: String, value: UShort = 0x0000u) : Register<UShort>(name, value)
    fun write(value: T) {
        this.value = value
    }
    fun read(): T {
        return value
    }
}