package com.tomassirio.cpu

import com.tomassirio.cpu.opcode.Command
import com.tomassirio.io.Display
import com.tomassirio.io.Keyboard
import com.tomassirio.memory.Memory
import com.tomassirio.cpu.utils.RegisterSet
import com.tomassirio.memory.toUShortAt
import com.tomassirio.utils.SizedStack

class CPU(
    val memory: Memory,
    val registers: RegisterSet,
    val keyboard: Keyboard,
    val display: Display,
    val pc: Register.ShortRegister,
    val sp: Register.ByteRegister,
    val I: Register.ShortRegister,

    val DT: Register.TimerRegister,
    val ST: Register.TimerRegister,

    val stack: SizedStack<UShort>,
    val commands: (UShort) -> Command
) {

    fun runCycle() {
        val opcode = fetch()
        val command = decode(opcode)
        execute(command, opcode)
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    private fun fetch(): UShort {
        return memory.read(pc.read().toInt()) { bytes, addr -> bytes.toUShortAt(addr) }
    }

    private fun decode(opcode: UShort): Command {
        return commands(opcode)
    }

    private fun execute(command: Command, opcode: UShort) {
        command.execute(this, opcode)
        pc.write(pc.read().plus(2u).toUShort())
    }

    fun debug() {
        StringBuilder().apply {
            append("CPU State:\n")
            append("Keyboard: ${keyboard}\n")
            append("Display: ${display}\n\n")
            append("Short Registers\n")
            append("$pc\n")
            append("$sp\n")
            append("$I\n\n")
            append("Byte Registers:\n")
            registers.forEach {
                append("$it\n")
            }
            append("\n")
            append("Memory: $memory\n")
            println(toString())
        }
    }
}