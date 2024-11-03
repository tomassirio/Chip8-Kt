package com.tomassirio.cpu

import com.tomassirio.cpu.opcode.Command
import com.tomassirio.cpu.opcode.SysAddrCommand
import com.tomassirio.io.Display
import com.tomassirio.io.Keyboard
import com.tomassirio.memory.Memory

class CPU(
    val memory: Memory,
    val registers: MutableSet<Register.ByteRegister>,
    val keyboard: Keyboard,
    val display: Display,
    var pc: Register.ShortRegister,
    var sp: Register.ByteRegister,
    var I: Register.ShortRegister,

    var stack: List<UShort>,
    private val opcodes: Map<UInt, Command> = defaultOpcodes
) {

    fun runCycle() {
        execute(decode(fetch()))
    }

    private fun fetch(): UShort {
        return memory.read<UShort>(pc.value.toInt())
    }

    private fun decode(opcode: UShort): Command {
        val maskedOpcode = opcode.and(0xFFFu)
        return opcodes[maskedOpcode.toUInt()] ?: throw IllegalArgumentException("Invalid OpCode")
    }

    private fun execute(command: Command) {
        command.execute(this)
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

    companion object {
        val defaultOpcodes: Map<UInt, Command> = mapOf(
            0x0000u to SysAddrCommand
        )
    }
}