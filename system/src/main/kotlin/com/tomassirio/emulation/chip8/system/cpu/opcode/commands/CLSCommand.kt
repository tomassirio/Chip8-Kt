package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.opcode.Command

/**
 * 00E0 - CLS
 * Clear the display.
 *
 */
fun clsCommand(): Command {
    return Command { cpu, _ ->
        cpu.displayState.clear()
    }
}
