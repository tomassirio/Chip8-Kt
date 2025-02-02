package com.tomassirio.cpu.opcode.commands

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.opcode.Command

/**
 * 4xkk - SNE Vx, byte
 * Skip next instruction if Vx != kk.
 *
 * The interpreter compares register Vx to kk, and if they are not equal, increments the program counter by 2.
 *
 */
object SNEVxByteCommand: Command{
    override fun execute(cpu: CPU, opcode: UShort) {
        val registerX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val value = (opcode and 0x00FFu).toUByte()
        if (registerX.read() != value) {
            cpu.pc.write(cpu.pc.read().plus(2u).toUShort())
        }
    }
}