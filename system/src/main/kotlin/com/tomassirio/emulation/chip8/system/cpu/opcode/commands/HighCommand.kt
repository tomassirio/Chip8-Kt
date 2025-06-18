package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.opcode.Command

/**
 * 00FF - HIGH
 * Enable extended screen mode
 */
fun highCommand() = Command { cpu, _ ->
    cpu.displayState.enableExtendedMode()
}