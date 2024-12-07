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
object SNEVxByteCommand: Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        val registerName: Int = (opcode.toInt() and 0x0F00) shr 8
        val register = cpu.registers[registerName.toString()]
        val value = (opcode and 0x00FFu).toUByte()
        if (register.read() != value) {
            cpu.pc.write(cpu.pc.read().plus(2u).toUShort())
        }
    }
}