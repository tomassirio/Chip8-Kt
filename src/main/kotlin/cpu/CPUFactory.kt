package com.tomassirio.cpu

import com.tomassirio.cpu.opcode.OpCodeTable
import com.tomassirio.io.Display
import com.tomassirio.io.Keyboard
import com.tomassirio.memory.MemoryFactory
import com.tomassirio.utils.SizedStack

object CPUFactory {

    private val emptyValue: UShort = 0x0000u

    fun createCPU(cpuType: CPUType = CPUType.CHIP8): CPU {
        return when (cpuType) {
            CPUType.CHIP8 -> createChip8CPU()
            CPUType.ETI660 -> createETI660CPU()
        }
    }

    private fun createChip8CPU(): CPU {
        return CPU(
            MemoryFactory.createMemory(),
            createRegisterSet(),
            Keyboard(),
            Display(),
            Register.ShortRegister("pc",0x200u),
            Register.ByteRegister("sp"),
            Register.ShortRegister("I"),
            createStack(),
            OpCodeTable.chip8CommandGetter
        )
    }

    private fun createETI660CPU(): CPU {
        return CPU(
            MemoryFactory.createETIMemory(),
            createRegisterSet(),
            Keyboard(),
            Display(),
            Register.ShortRegister("pc", 0x600u),
            Register.ByteRegister("sp"),
            Register.ShortRegister("I"),
            createStack(),
            OpCodeTable.chip48CommandGetter
        )
    }

    private fun createRegisterSet(): MutableSet<Register.ByteRegister> {
        return mutableSetOf(
            Register.ByteRegister("v0"),
            Register.ByteRegister("v1"),
            Register.ByteRegister("v2"),
            Register.ByteRegister("v3"),
            Register.ByteRegister("v4"),
            Register.ByteRegister("v5"),
            Register.ByteRegister("v6"),
            Register.ByteRegister("v7"),
            Register.ByteRegister("v8"),
            Register.ByteRegister("v9"),
            Register.ByteRegister("vA"),
            Register.ByteRegister("vB"),
            Register.ByteRegister("vC"),
            Register.ByteRegister("vD"),
            Register.ByteRegister("vE"),
            Register.ByteRegister("vF") //Flag register
        )
    }

    private fun createStack(): SizedStack<UShort> {
        return SizedStack<UShort>(16).apply {
            push(emptyValue)
        }
    }
}