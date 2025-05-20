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
            pc =Register.ShortRegister(value = 0x200u),
            sp = Register.ByteRegister(),
            I = Register.ShortRegister(),
            DT = Register.TimerRegister(),
            ST = Register.TimerRegister(),
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
            pc = Register.ShortRegister(0x600u),
            sp = Register.ByteRegister(),
            I = Register.ShortRegister(),
            DT = Register.TimerRegister(),
            ST = Register.TimerRegister(),
            createStack(),
            OpCodeTable.chip48CommandGetter
        )
    }

    private fun createRegisterSet(): RegisterSet {
        return RegisterSet.Builder().apply {
            for (i in 0..0xf) {
                addRegister(i.toShort())
            }
        }.build()
    }

    private fun createStack(): SizedStack<UShort> {
        return SizedStack<UShort>(16).apply {
            push(emptyValue)
        }
    }
}