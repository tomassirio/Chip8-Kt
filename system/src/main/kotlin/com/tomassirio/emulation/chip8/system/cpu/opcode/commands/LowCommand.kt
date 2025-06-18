package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.opcode.Command

/**
 * 00FE - LOW
 * Disable extended screen mode
 */
fun lowCommand() = Command { cpu, _ ->
    cpu.displayState.disableExtendedMode()
}