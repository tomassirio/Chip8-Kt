package io.github.tomassirio.system.cpu

import io.github.tomassirio.system.cpu.opcode.Command
import io.github.tomassirio.system.cpu.opcode.OpCodeTable
import io.github.tomassirio.system.cpu.utils.SizedStack
import io.github.tomassirio.system.io.display.DisplayState
import io.github.tomassirio.system.io.keyboard.KeyboardState
import io.github.tomassirio.system.memory.Memory
import io.github.tomassirio.system.memory.accessor.MemoryAccessor
import io.github.tomassirio.system.register.Register
import io.github.tomassirio.system.register.utils.RegisterSet

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