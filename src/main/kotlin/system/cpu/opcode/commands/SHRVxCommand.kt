package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.Command

/**
 * 8xy6 - SHR Vx {, Vy}
 * Set Vx = Vx SHR 1.
 *
 * If the least significant bit of Vx is 1, then VF is set to 1, otherwise 0. Then Vx is divided by 2.
*/
fun shrVxCommand(): Command {
    return Command { cpu, opcode ->
        val x = (opcode and 0x0F00u).toInt() shr 8
        val y = (opcode and 0x00F0u).toInt() shr 4
        val vy = cpu.registers[y].read()

        cpu.registers[0xF].write(if (vy.toInt() and 0x1 == 1) 1u else 0u)
        cpu.registers[x].write((vy.toInt() shr 1).toUByte())
    }
}