package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.Command

/**
 * Fx07 - LD Vx, DT
 * Set Vx = delay timer value.
 *
 * The value of DT is placed into Vx.
 */
fun ldVxDTCommand(): Command {
    return Command {cpu, opcode ->
        val vX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        vX.write(cpu.DT.read())
    }
}