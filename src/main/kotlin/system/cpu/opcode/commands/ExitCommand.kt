package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.Command

/**
 * 00FD - EXIT
 * Exit CHIP interpreter
 */
fun exitCommand() = Command { cpu, _ ->
    cpu.halted = true
}