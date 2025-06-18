package com.tomassirio.emulation.chip8.system.cpu

import com.tomassirio.emulation.chip8.system.cpu.opcode.Command
import com.tomassirio.emulation.chip8.system.cpu.opcode.OpCodeTable
import com.tomassirio.emulation.chip8.system.cpu.utils.SizedStack
import com.tomassirio.emulation.chip8.system.io.display.DisplayState
import com.tomassirio.emulation.chip8.system.io.keyboard.KeyboardState
import com.tomassirio.emulation.chip8.system.memory.Memory
import com.tomassirio.emulation.chip8.system.memory.accessor.MemoryAccessor
import com.tomassirio.emulation.chip8.system.register.Register
import com.tomassirio.emulation.chip8.system.register.utils.RegisterSet

@OptIn(ExperimentalUnsignedTypes::class)
class CPU(
    val cpuType: CPUType,
    val memory: Memory,
    val registers: RegisterSet,
    val keyboardState: KeyboardState,
    val displayState: DisplayState,
    val pc: Register.ShortRegister,
    val I: Register.ShortRegister,
    val DT: Register.TimerRegister,
    val ST: Register.TimerRegister,
    val stack: SizedStack<UShort>,
    val rplFlags: UByteArray?
) {
    var halted = false

    fun runCycle() {
        if (halted || keyboardState.isWaitingForKey) return

        val opcode = fetch()
        val command = decode(opcode)
        execute(command, opcode)
    }

    private fun fetch(): UShort {
        return MemoryAccessor.readUShort(memory, pc.read().toInt())
    }

    private fun decode(opcode: UShort): Command {
        return when (cpuType) {
            CPUType.CHIP8 -> OpCodeTable.chip8CommandGetter(opcode)
            CPUType.SCHIP8 -> OpCodeTable.chip48CommandGetter(opcode)
        }
    }

    private fun execute(command: Command, opcode: UShort) {
        command.execute(this, opcode)
        if (!command.skipsPcIncrement) {
            pc.write(pc.read().plus(2u).toUShort())
        }
    }
}