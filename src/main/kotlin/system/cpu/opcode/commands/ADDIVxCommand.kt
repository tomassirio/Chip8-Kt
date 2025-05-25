package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.Command

/**
 * Fx1E - ADD I, Vx
 * Set I = I + Vx.
 *
 * The values of I and Vx are added, and the results are stored in I.
 *
 */
fun addIVxCommand(): Command {
    return Command { cpu, opcode ->
        val registerX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val value = registerX.read().toUShort()
        cpu.I.write((cpu.I.read() + value).toUShort())
    }
}