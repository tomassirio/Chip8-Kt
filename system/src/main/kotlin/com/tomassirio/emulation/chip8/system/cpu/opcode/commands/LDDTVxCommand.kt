package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.opcode.Command

/**
 * Fx15 - LD DT, Vx
 * Set delay timer = Vx.
 *
 * DT is set equal to the value of Vx.
 *
 */
fun ldDTVxCommand(): Command {
    return Command { cpu, opcode ->
        val registerX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val value = registerX.read()

        cpu.DT.write(value)
    }
}