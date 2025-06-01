package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.Command

/**
 * 8xyE - SHL Vx {, Vy}
 * Set Vx = Vx SHL 1.
 *
 * If the most significant bit of Vx is 1, then VF is set to 1. Otherwise, VF is set to 0. Then Vx is multiplied by 2.
*/
fun shlVxCommand(): Command {
    return Command {cpu, opcode ->
        val vX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val vXValue = vX.read()

        cpu.registers[0xF].write(if (vXValue.toInt() and 0x80 == 0x80) 1u else 0u)
        vX.write(vXValue.toInt().shl(1).toUByte())
    }
}