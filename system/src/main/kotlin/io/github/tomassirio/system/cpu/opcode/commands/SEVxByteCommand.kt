package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.cpu.opcode.Command

/**
 * 3xkk - SE Vx, byte
 * Skip next instruction if Vx = kk.
 *
 * The interpreter compares register Vx to kk, and if they are equal, increments the program counter by 2.
 */
fun seVxByteCommand(): Command {
    return Command { cpu, opcode ->
        val registerX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val value = (opcode and 0x00FFu).toUByte()
        if (registerX.read() == value) {
            cpu.pc.write(cpu.pc.read().plus(2u).toUShort())
        }
    }
}