package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.Command

/**
 * FX30 - LD HF, Vx
 * Point I to 10-byte font sprite for digit VX (0..9)
 */
fun ldhfVxCommand() = Command { cpu, opcode ->
    val x = (opcode and 0x0F00u).toInt() shr 8
    val vx = cpu.registers[x].read().toInt()
    cpu.I.write((vx * 10).toUShort())
}