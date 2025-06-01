package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.Command

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
