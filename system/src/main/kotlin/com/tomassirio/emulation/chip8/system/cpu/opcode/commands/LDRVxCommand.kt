package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.opcode.Command

/**
 * FX75 - LD R, Vx
 * Store V0 through VX in RPL user flags (X <= 7)
 */
@OptIn(ExperimentalUnsignedTypes::class)
fun ldRVxCommand() = Command { cpu, opcode ->
    val x = (opcode and 0x0F00u).toInt() shr 8
    for (i in 0..x) {
        cpu.rplFlags?.set(i, cpu.registers[i].read())
    }
}