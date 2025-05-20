package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.opcode.Command

/**
 * 8xy0 - LD Vx, Vy
 * Set Vx = Vy.
 *
 * Stores the value of register Vy in register Vx.
 */
object LDVxVyCommand: Command{
    override fun execute(cpu: CPU, opcode: UShort) {
        val registerX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val registerY = cpu.registers[(opcode and 0xF0u).toInt() shr 4]

        registerX.write(registerY.read())
    }
}