package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.opcode.Command

/**
 *   8xy3 - XOR Vx, Vy
 *   Set Vx = Vx XOR Vy.
 *
 * Performs a bitwise exclusive OR on the values of Vx and Vy,
 * then stores the result in Vx.
 * An exclusive OR compares the corresponding bits from two values,
 * and if the bits are not both the same, then the corresponding bit in the result is set to 1.
 * Otherwise, it is 0.
 */
fun xorVxVyCommand(): Command {
    return Command { cpu, opcode ->
        val registerX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val registerY = cpu.registers[(opcode and 0xF0u).toInt() shr 4]

        registerX.write((registerX.read() xor registerY.read()))
        cpu.registers[0xF].write(0x0.toUByte())
    }
}