package com.tomassirio.cpu.opcode.commands

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.opcode.Command

/**
 *   7xkk - ADD Vx, byte
 *   Set Vx = Vx + kk.
 *
 *   Adds the value kk to the value of register Vx, then stores the result in Vx.
 */
object ADDVxByteCommand: Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        val registerName = (opcode.toInt() and 0x0F00) shr 8
        val register = cpu.registers[registerName.toString()]
        val value = (opcode and 0x00FFu).toUByte()
        register.write((register.read() + value).toUByte())
    }
}