package com.tomassirio.utils

import com.tomassirio.cpu.Register
import com.tomassirio.cpu.exception.RegisterNotFoundException

class RegisterSet private constructor(private val registers: List<Register.ByteRegister>) : Iterable<Register.ByteRegister> {
    private val registerMap: Map<String, Register.ByteRegister> = registers.associateBy { it.name }

    operator fun get(name: String): Register.ByteRegister =
        registerMap[name] ?: throw RegisterNotFoundException(name)

    override fun iterator(): Iterator<Register.ByteRegister> = registers.iterator()

    class Builder {
        private val registers = mutableListOf<Register.ByteRegister>()

        fun addRegister(name: String, initialValue: UByte = 0u): Builder {
            registers.add(Register.ByteRegister(name, initialValue))
            return this
        }

        fun build(): RegisterSet = RegisterSet(registers)
    }
}