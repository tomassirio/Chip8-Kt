package com.tomassirio.cpu.opcode.commands

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.opcode.Command

/**
 * 9xy0 - SNE Vx, Vy
 * Skip the next instruction if Vx != Vy.
 *
 * Compare register Vx to register Vy, and skip the next instruction if they are not equal.
 */

object SNEVxVyCommand : Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        val vX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val vY = cpu.registers[(opcode and 0xF0u).toInt() shr 4]

        if (vX.read() != vY.read()) {
            cpu.pc.write(cpu.pc.read().plus(2u).toUShort())
        }
    }

}
