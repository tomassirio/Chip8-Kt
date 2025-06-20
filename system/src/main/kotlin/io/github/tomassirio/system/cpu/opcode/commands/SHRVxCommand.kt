package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.cpu.opcode.Command

/**
 * 8xy6 - SHR Vx {, Vy}
 * Set Vx = Vx SHR 1.
 *
 * If the least significant bit of Vx is 1, then VF is set to 1, otherwise 0. Then Vx is divided by 2.
 */
fun shrVxCommand(): Command {
    return Command { cpu, opcode ->
        val x = (opcode and 0x0F00u).toInt() shr 8
        val vx = cpu.registers[x].read()

        cpu.registers[0xF].write(if (vx.toInt() and 0x01 == 1) 1u else 0u)
        cpu.registers[x].write((vx.toInt() shr 1).toUByte())
    }
}