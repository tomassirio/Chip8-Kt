package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.opcode.Command


/**
 * Fx18 - LD ST, Vx
 *     Set sound timer = Vx.
 *
 *     ST is set equal to the value of Vx.
 */
fun ldSTVxCommand(): Command {
    return Command { cpu, opcode ->
        val registerX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val value = registerX.read()

        cpu.ST.write(value)
    }
}