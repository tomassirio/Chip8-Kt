package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.opcode.Command

/**
    *8xy7 - SUBN Vx, Vy
    *Set Vx = Vy - Vx, set VF = NOT borrow.
    *
    *If Vy > Vx, then VF is set to 1, otherwise 0. Then Vx is subtracted from Vy, and the results stored in Vx.
*/

object SUBNVxVyCommand : Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        val vX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val vY = cpu.registers[(opcode and 0xF0u).toInt() shr 4]

        cpu.registers[0xF].write(if (vY.read() > vX.read()) 1u else 0u)
        val result = vY.read().minus(vX.read()).toUByte()
        vX.write(result)
    }
}
