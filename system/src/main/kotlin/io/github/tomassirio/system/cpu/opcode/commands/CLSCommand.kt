package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.cpu.opcode.Command

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
