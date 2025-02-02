package com.tomassirio.cpu.opcode.commands

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.opcode.Command

/**
 * 6xkk - LD Vx, byte
 * Set Vx = kk.
 *
 * The interpreter puts the value kk into register Vx.
 */
object LDVxByteCommand: Command{
    override fun execute(cpu: CPU, opcode: UShort) {
        val registerX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val value = (opcode and 0x00FFu).toUByte()
        registerX.write(value)
    }
}