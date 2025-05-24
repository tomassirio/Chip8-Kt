package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.opcode.Command


/**
 * Fx18 - LD ST, Vx
 *     Set sound timer = Vx.
 *
 *     ST is set equal to the value of Vx.
 */
object LDSTVxCommand: Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        val registerX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val value = registerX.read()

        cpu.ST.write(value)
    }
}