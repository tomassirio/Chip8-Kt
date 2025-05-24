package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.opcode.Command

/**
 * Fx15 - LD DT, Vx
 * Set delay timer = Vx.
 *
 * DT is set equal to the value of Vx.
 *
 */
object LDDTVxCommand: Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        val registerX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val value = registerX.read()

        cpu.DT.write(value)
    }
}