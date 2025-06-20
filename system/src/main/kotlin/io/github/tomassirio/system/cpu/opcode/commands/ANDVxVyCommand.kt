package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.cpu.opcode.Command

/**
 *   8xy2 - AND Vx, Vy
 *   Set Vx = Vx AND Vy.
 *
 * Performs a bitwise AND on the values of Vx and Vy,
 * then stores the result in Vx.
 * A bitwise AND compares the corresponding bits from two values,
 * and if both bits are 1, then the same bit in the result is also 1.
 * Otherwise, it is 0.
 */
fun andVxVyCommand(): Command {
    return Command { cpu, opcode ->
        val registerX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val registerY = cpu.registers[(opcode and 0xF0u).toInt() shr 4]

        registerX.write((registerX.read() and registerY.read()))
        cpu.registers[0xF].write(0x0.toUByte())
    }
}