package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.Command

/**
 * 8xy5 - SUB Vx, Vy
 * Set Vx = Vx - Vy, set VF = NOT borrow.
 *
 * If Vx > Vy, then VF is set to 1, otherwise 0. Then Vx is subtracted by Vy, and the results stored in Vx.
*/
fun subVxVyCommand(): Command {
    return Command {cpu, opcode ->
        val vX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val vY = cpu.registers[(opcode and 0xF0u).toInt() shr 4]

        val result = vX.read().minus(vY.read())
        cpu.registers[0xF].write(if (vX.read() > vY.read()) 1u else 0u)
        vX.write(result.toUByte())
    }
}