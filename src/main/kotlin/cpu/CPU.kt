package com.tomassirio.cpu

import com.tomassirio.cpu.opcode.Command
import com.tomassirio.cpu.opcode.OpcodeTable
import com.tomassirio.io.Display
import com.tomassirio.io.Keyboard
import com.tomassirio.memory.Memory
import com.tomassirio.utils.SizedStack

class CPU(
    val memory: Memory,
    val registers: MutableSet<Register.ByteRegister>,
    val keyboard: Keyboard,
    val display: Display,
    val pc: Register.ShortRegister,
    val sp: Register.ByteRegister,
    val I: Register.ShortRegister,

    val stack: SizedStack<UShort>,
    val opcodeTable: OpcodeTable = OpcodeTable()
) {

    fun runCycle() {
        val opcode = fetch()
        val command = decode(opcode)
        execute(command, opcode)
    }

    private fun fetch(): UShort {
        return memory.read<UShort>(pc.value.toInt())
    }

    private fun decode(opcode: UShort): Command {
        return opcodeTable.getCommand(opcode)
    }

    private fun execute(command: Command, opcode: UShort) {
        command.execute(this, opcode)
        pc.value = pc.value.plus(2u).toUShort()
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