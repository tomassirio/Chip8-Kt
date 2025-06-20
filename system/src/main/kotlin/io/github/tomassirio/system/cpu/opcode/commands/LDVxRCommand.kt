package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.cpu.opcode.Command

/**
 * FX85 - LD Vx, R
 * Read V0 through VX from RPL user flags (X <= 7)
 */
@OptIn(ExperimentalUnsignedTypes::class)
fun ldVxRCommand() = Command { cpu, opcode ->
    val x = (opcode and 0x0F00u).toInt() shr 8
    for (i in 0..x) {
        cpu.rplFlags?.get(i)?.let { cpu.registers[i].write(it) }
    }
}