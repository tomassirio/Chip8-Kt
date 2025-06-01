package com.tomassirio.system.cpu

import com.tomassirio.system.cpu.opcode.Command
import com.tomassirio.system.cpu.opcode.OpCodeTable
import com.tomassirio.system.register.utils.RegisterSet
import com.tomassirio.system.memory.Memory
import com.tomassirio.system.memory.util.toUShortAt
import com.tomassirio.system.cpu.utils.SizedStack
import com.tomassirio.system.io.DisplayState
import com.tomassirio.system.io.KeyboardState
import com.tomassirio.system.register.Register

class CPU(
    val memory: Memory,
    val registers: RegisterSet,
    val keyboardState: KeyboardState,
    val displayState: DisplayState,
    val pc: Register.ShortRegister,
    val sp: Register.ByteRegister,
    val I: Register.ShortRegister,

    val DT: Register.TimerRegister,
    val ST: Register.TimerRegister,

    val stack: SizedStack<UShort>,
) {
    var waitingForKey: Boolean = false
    var registerToStoreKeyIn: Int? = null

    fun loadRom(data: ByteArray) {
        data.forEachIndexed { index, byte ->
            memory.write(
                memory.startAddress + index,
                byte.toUByte()
            )
        }
    }

    fun runCycle() {
        if (waitingForKey) return

        val opcode = fetch()
        val command = decode(opcode)
        execute(command, opcode)
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    private fun fetch(): UShort {
        return memory.read(pc.read().toInt()) { bytes, addr -> bytes.toUShortAt(addr) }
    }

    private fun decode(opcode: UShort): Command {
        return OpCodeTable.getCommand(opcode)
    }

    private fun execute(command: Command, opcode: UShort) {
        command.execute(this, opcode)
        if(!command.skipsPcIncrement) {
            pc.write(pc.read().plus(2u).toUShort())
        }
    }
}