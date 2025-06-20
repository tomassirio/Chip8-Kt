package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.cpu.opcode.Command

/**
 * 8xyE - SHL Vx {, Vy}
 * Set Vx = Vx SHL 1.
 *
 * If the most significant bit of Vx is 1, then VF is set to 1. Otherwise, VF is set to 0. Then Vx is multiplied by 2.
 */
fun shlVxCommand(): Command {
    return Command { cpu, opcode ->
        val x = (opcode and 0x0F00u).toInt() shr 8
        val vx = cpu.registers[x].read()

        cpu.registers[0xF].write(if (vx.toInt() and 0x80 == 0x80) 1u else 0u)
        cpu.registers[x].write((vx.toInt() shl 1).toUByte())
    }
}