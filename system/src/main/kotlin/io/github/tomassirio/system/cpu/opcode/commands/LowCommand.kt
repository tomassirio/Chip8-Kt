package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.cpu.opcode.Command

/**
 * 00FE - LOW
 * Disable extended screen mode
 */
fun lowCommand() = Command { cpu, _ ->
    cpu.displayState.disableExtendedMode()
}