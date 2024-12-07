package com.tomassirio.cpu.utils

import com.tomassirio.cpu.Register
import com.tomassirio.cpu.exception.RegisterNotFoundException

class RegisterSet<T : Register<*>> private constructor(
    private val registers: List<T>
) : Iterable<T> {
    private val registerMap: Map<String, T> = registers.associateBy { it.name }

    operator fun get(name: String): T = registerMap[name] ?: throw RegisterNotFoundException(name)

    override fun iterator(): Iterator<T> = registers.iterator()

    class Builder<T : Register<*>> {
        private val registers = mutableListOf<T>()
        private var registerInitializer: (String, UByte) -> T = { name, initialValue ->
            @Suppress("UNCHECKED_CAST")
            Register.ByteRegister(name, initialValue) as T
        }

        fun addRegister(name: String, initialValue: UByte = 0u, register: T? = null): Builder<T> {
            registers.add(register ?: registerInitializer(name, initialValue))
            return this
        }

        fun withRegisterInitializer(initializer: (String, UByte) -> T): Builder<T> {
            this.registerInitializer = initializer
            return this
        }

        fun build(): RegisterSet<T> {
            return RegisterSet(registers)
        }
    }
}