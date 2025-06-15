package com.tomassirio.system.cpu.factory

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.CPUType
import com.tomassirio.system.cpu.utils.SizedStack
import com.tomassirio.system.io.DisplayState
import com.tomassirio.system.io.KeyboardState
import com.tomassirio.system.memory.factory.MemoryFactory
import com.tomassirio.system.register.Register
import com.tomassirio.system.register.utils.RegisterSet

object CPUFactory {
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
            keyboardState = KeyboardState(),
            displayState = DisplayState(),
            pc = Register.ShortRegister(value = 0x200u),
            I = Register.ShortRegister(),
            DT = Register.TimerRegister(),
            ST = Register.TimerRegister(),
            createStack(),
        )
    }

    private fun createETI660CPU(): CPU {
        return CPU(
            MemoryFactory.createETIMemory(),
            createRegisterSet(),
            keyboardState = KeyboardState(),
            displayState = DisplayState(),
            pc = Register.ShortRegister(0x600u),
            I = Register.ShortRegister(),
            DT = Register.TimerRegister(),
            ST = Register.TimerRegister(),
            createStack(),
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
        return SizedStack(16)
    }
}