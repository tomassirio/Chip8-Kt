package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.Command

/**
 *   7xkk - ADD Vx, byte
 *   Set Vx = Vx + kk.
 *
 *   Adds the value kk to the value of register Vx, then stores the result in Vx.
 */
fun addVxByteCommand(): Command {
    return Command { cpu, opcode ->
        val registerX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]

        val value = (opcode and 0x00FFu).toUByte()
        registerX.write((registerX.read() + value).toUByte())
    }
}