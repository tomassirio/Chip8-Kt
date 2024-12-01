package com.tomassirio.cpu.opcode.commands

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.opcode.Command

/**
 * 3xkk - SE Vx, byte
 * Skip next instruction if Vx = kk.
 *
 * The interpreter compares register Vx to kk, and if they are equal, increments the program counter by 2.
 */
object SEVxByteCommand : Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        val registerName = (opcode.toInt() and 0x0F00) shr 8
        val register = cpu.registers[registerName.toString()]
        val value = (opcode and 0x00FFu).toUByte()
        if (register.read() == value) {
            cpu.pc.write(cpu.pc.read().plus(2u).toUShort())
        }
    }
}