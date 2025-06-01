package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.Command

/**
 * Fx29 - LD F, Vx
 * Set I = location of sprite for digit Vx.
 *
 * The value of I is set to the location for the hexadecimal sprite corresponding to the value of Vx. See section 2.4,
 *  Display, for more information on the Chip-8 hexadecimal font.
 */
fun ldFVxCommand(): Command {
    return Command {cpu, opcode ->
        val x = (opcode and 0x0F00u).toInt() shr 8
        val vx = cpu.registers[x].read()

        val spriteAddress = 0x00u.plus(vx * 5u).toUShort()
        cpu.I.write(spriteAddress)
    }
}