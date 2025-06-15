package com.tomassirio.system.cpu

import com.tomassirio.system.cpu.opcode.Command
import com.tomassirio.system.cpu.opcode.OpCodeTable
import com.tomassirio.system.cpu.utils.SizedStack
import com.tomassirio.system.io.DisplayState
import com.tomassirio.system.io.KeyboardState
import com.tomassirio.system.memory.Memory
import com.tomassirio.system.memory.accessor.MemoryAccessor
import com.tomassirio.system.register.Register
import com.tomassirio.system.register.utils.RegisterSet

class CPU(
    val memory: Memory,
    val registers: RegisterSet,
    val keyboardState: KeyboardState,
    val displayState: DisplayState,
    val pc: Register.ShortRegister,
    val I: Register.ShortRegister,
    val DT: Register.TimerRegister,
    val ST: Register.TimerRegister,
    val stack: SizedStack<UShort>,
) {

    fun runCycle() {
        if (keyboardState.isWaitingForKey) return

        val opcode = fetch()
        val command = decode(opcode)
        execute(command, opcode)
    }

    private fun fetch(): UShort {
        return MemoryAccessor.readUShort(memory, pc.read().toInt())
    }

    private fun decode(opcode: UShort): Command {
        return OpCodeTable.getCommand(opcode)
    }

    private fun execute(command: Command, opcode: UShort) {
        command.execute(this, opcode)
        if (!command.skipsPcIncrement) {
            pc.write(pc.read().plus(2u).toUShort())
        }
    }
}