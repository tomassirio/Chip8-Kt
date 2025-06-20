package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.constants.FONTSET_BASE_ADDRESS
import io.github.tomassirio.system.cpu.opcode.Command

/**
 * Fx29 - LD F, Vx
 * Set I = location of sprite for digit Vx.
 *
 * The value of I is set to the location for the hexadecimal sprite corresponding to the value of Vx. See section 2.4,
 *  Display, for more information on the Chip-8 hexadecimal font.
 */
fun ldFVxCommand(): Command {
    return Command { cpu, opcode ->
        val vx = cpu.registers[(opcode and 0x0F00u).toInt() shr 8]
        val value = vx.read()

        val spriteAddress = FONTSET_BASE_ADDRESS.plus(value * 5u).toUShort()
        cpu.I.write(spriteAddress)
    }
}