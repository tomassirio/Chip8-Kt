package com.tomassirio.cpu.opcode.commands

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.opcode.Command

/**
 * 6xkk - LD Vx, byte
 * Set Vx = kk.
 *
 * The interpreter puts the value kk into register Vx.
 */
object LDVxByteCommand: Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        val registerName = (opcode.toInt() and 0x0F00) shr 8
        val register = cpu.registers[registerName.toString()]
        val value = (opcode and 0x00FFu).toUByte()
        register.write(value)
    }
}