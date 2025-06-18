package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.opcode.Command

/**
 * 00CN - SCD N
 * Scroll display N lines down
 */
fun scdNCommand() = Command { cpu, opcode ->
    val n = (opcode and 0x000Fu).toInt()
    cpu.displayState.scrollDown(n)
}
