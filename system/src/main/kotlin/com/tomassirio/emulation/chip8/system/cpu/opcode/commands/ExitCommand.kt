package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.opcode.Command

/**
 * 00FD - EXIT
 * Exit CHIP interpreter
 */
fun exitCommand() = Command { cpu, _ ->
    cpu.halted = true
}