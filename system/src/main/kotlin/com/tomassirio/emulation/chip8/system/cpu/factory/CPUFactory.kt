package com.tomassirio.emulation.chip8.system.cpu.factory

import com.tomassirio.emulation.chip8.system.cpu.CPU
import com.tomassirio.emulation.chip8.system.cpu.CPUType
import com.tomassirio.emulation.chip8.system.cpu.utils.SizedStack
import com.tomassirio.emulation.chip8.system.io.display.DisplayState
import com.tomassirio.emulation.chip8.system.io.keyboard.KeyboardState
import com.tomassirio.emulation.chip8.system.memory.factory.MemoryFactory
import com.tomassirio.emulation.chip8.system.register.Register
import com.tomassirio.emulation.chip8.system.register.utils.RegisterSet

object CPUFactory {
    fun createCPU(cpuType: CPUType = CPUType.CHIP8): CPU {
        return when (cpuType) {
            CPUType.CHIP8 -> createChip8CPU()
            CPUType.SCHIP8 -> createSuperChip8CPU()
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    private fun createChip8CPU(): CPU {
        return CPU(
            CPUType.CHIP8,
            MemoryFactory.createMemory(),
            createRegisterSet(),
            keyboardState = KeyboardState(),
            displayState = DisplayState(),
            pc = Register.ShortRegister(value = 0x200u),
            I = Register.ShortRegister(),
            DT = Register.TimerRegister(),
            ST = Register.TimerRegister(),
            createStack(),
            null
        )
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    private fun createSuperChip8CPU(): CPU {
        return CPU(
            CPUType.SCHIP8,
            MemoryFactory.createMemory(),
            createRegisterSet(),
            keyboardState = KeyboardState(),
            displayState = DisplayState(),
            pc = Register.ShortRegister(0x200u),
            I = Register.ShortRegister(),
            DT = Register.TimerRegister(),
            ST = Register.TimerRegister(),
            createStack(),
            UByteArray(8) { 0u }
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