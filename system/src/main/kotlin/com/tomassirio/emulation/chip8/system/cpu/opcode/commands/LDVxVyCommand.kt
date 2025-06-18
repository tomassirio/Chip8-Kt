package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.opcode.Command

/**
 * 8xy0 - LD Vx, Vy
 * Set Vx = Vy.
 *
 * Stores the value of register Vy in register Vx.
 */
fun ldVxVyCommand(): Command {
    return Command { cpu, opcode ->
        val registerX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val registerY = cpu.registers[(opcode and 0xF0u).toInt() shr 4]

        registerX.write(registerY.read())
    }
}