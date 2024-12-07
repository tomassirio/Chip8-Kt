package com.tomassirio.cpu.opcode.commands

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.opcode.Command

/**
 * 8xy1 - OR Vx, Vy
 * Set Vx = Vx OR Vy.
 *
 * Performs a bitwise OR on the values of Vx and Vy, then stores the result in Vx.
 */
object ORVxVyCommand: Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        val registerXName = (opcode.toInt() and 0x0F00) shr 8
        val registerYName = (opcode.toInt() and 0x00F0) shr 4
        val registerX = cpu.registers[registerXName.toString()]
        val registerY = cpu.registers[registerYName.toString()]

        registerX.write(registerX.read() or registerY.read())
    }
}