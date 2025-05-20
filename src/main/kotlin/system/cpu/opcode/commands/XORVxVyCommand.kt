package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.opcode.Command

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

object XORVxVyCommand: Command{
    override fun execute(cpu: CPU, opcode: UShort) {
        val registerX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val registerY = cpu.registers[(opcode and 0xF0u).toInt() shr 4]

        registerX.write((registerX.read() xor registerY.read()))
    }
}