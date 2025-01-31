package com.tomassirio.cpu.opcode.commands

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.opcode.Command

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

object ANDVxVyCommand: Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        val registerXName = (opcode.toInt() and 0x0F00) shr 8
        val registerX = cpu.registers[registerXName.toString()]

        val registerYName = (opcode.toInt() and 0x00F0) shr 4
        val registerY = cpu.registers[registerYName.toString()]

        registerX.write((registerX.read() and registerY.read()))
    }
}