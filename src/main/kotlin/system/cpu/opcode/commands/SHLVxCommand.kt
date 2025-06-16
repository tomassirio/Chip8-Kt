package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.Command

/**
 * 8xyE - SHL Vx {, Vy}
 * Set Vx = Vx SHL 1.
 *
 * If the most significant bit of Vx is 1, then VF is set to 1. Otherwise, VF is set to 0. Then Vx is multiplied by 2.
*/
fun shlVxCommand(): Command {
    return Command { cpu, opcode ->
        val x = (opcode and 0x0F00u).toInt() shr 8
        val y = (opcode and 0x00F0u).toInt() shr 4
        val vy = cpu.registers[y].read()

        cpu.registers[0xF].write(if (vy.toInt() and 0x80 == 0x80) 1u else 0u)
        cpu.registers[x].write((vy.toInt() shl 1).toUByte())
    }
}