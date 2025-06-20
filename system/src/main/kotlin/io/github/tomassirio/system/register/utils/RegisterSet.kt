package io.github.tomassirio.system.register.utils

import io.github.tomassirio.system.cpu.exception.RegisterNotFoundException
import io.github.tomassirio.system.register.Register


class RegisterSet private constructor(
    private val registers: MutableMap<Int, Register.ByteRegister>
) : Iterable<Register.ByteRegister> {

    operator fun get(index: Int): Register.ByteRegister {
        return registers[index] ?: throw RegisterNotFoundException(index.toShort())
    }

    override fun iterator(): Iterator<Register.ByteRegister> = registers.values.iterator()

    class Builder {
        private val registers = mutableMapOf<Int, Register.ByteRegister>()
        private var registerInitializer: (Short, UByte) -> Register.ByteRegister = { index, initialValue ->
            Register.ByteRegister(index, initialValue)
        }

        fun addRegister(index: Short, initialValue: UByte = 0u, register: Register.ByteRegister? = null): Builder {
            registers[index.toInt()] = register ?: registerInitializer(index, initialValue)
            return this
        }

        fun build(): RegisterSet {
            return RegisterSet(registers)
        }
    }
}