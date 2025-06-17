package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.Command

/**
 * 00FF - HIGH
 * Enable extended screen mode
 */
fun highCommand() = Command { cpu, _ ->
    cpu.displayState.enableExtendedMode()
}