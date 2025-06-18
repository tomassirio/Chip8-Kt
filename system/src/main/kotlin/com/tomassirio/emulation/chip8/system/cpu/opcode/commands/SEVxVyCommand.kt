package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.opcode.Command

/**
 * 5xy0 - SE Vx, Vy
 * Skip next instruction if Vx = Vy.
 *
 * The interpreter compares register Vx to register Vy, and if they are equal, increments the program counter by 2.
 */
fun seVxVyCommand(): Command {
    return Command { cpu, opcode ->
        val registerX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val registerY = cpu.registers[(opcode and 0xF0u).toInt() shr 4]

        if (registerX.read() == registerY.read()) {
            cpu.pc.write(cpu.pc.read().plus(2u).toUShort())
        }
    }
}