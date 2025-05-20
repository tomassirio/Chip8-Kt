package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.opcode.Command

/**
 * 8xy1 - OR Vx, Vy
 * Set Vx = Vx OR Vy.
 *
 * Performs a bitwise OR on the values of Vx and Vy, then stores the result in Vx.
 */
object ORVxVyCommand: Command{
    override fun execute(cpu: CPU, opcode: UShort) {
        val registerX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val registerY = cpu.registers[(opcode and 0xF0u).toInt() shr 4]

        registerX.write(registerX.read() or registerY.read())
    }
}