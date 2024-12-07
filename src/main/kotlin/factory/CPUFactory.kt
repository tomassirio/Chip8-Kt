package com.tomassirio.factory

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.CPUType
import com.tomassirio.cpu.Register
import com.tomassirio.cpu.opcode.OpCodeTable
import com.tomassirio.io.Display
import com.tomassirio.io.Keyboard
import com.tomassirio.cpu.utils.RegisterSet
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

    private fun createRegisterSet(): RegisterSet<Register.ByteRegister> {
        return RegisterSet.Builder<Register.ByteRegister>()
            .addRegister("0")
            .addRegister("1")
            .addRegister("2")
            .addRegister("3")
            .addRegister("4")
            .addRegister("5")
            .addRegister("6")
            .addRegister("7")
            .addRegister("8")
            .addRegister("9")
            .addRegister("A")
            .addRegister("B")
            .addRegister("C")
            .addRegister("D")
            .addRegister("E")
            .addRegister("F") // Flag register
            .build()
    }

    private fun createStack(): SizedStack<UShort> {
        return SizedStack<UShort>(16).apply {
            push(emptyValue)
        }
    }
}