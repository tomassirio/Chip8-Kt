package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.Command

/**
 * 00FB - SCR
 * Scroll display 4 pixels right
 */
fun scrCommand() = Command { cpu, _ ->
    cpu.displayState.scrollRight(4)
}