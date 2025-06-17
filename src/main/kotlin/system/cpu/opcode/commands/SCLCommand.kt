package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.Command

/**
 * 00FC - SCL
 * Scroll display 4 pixels left
 */
fun sclCommand() = Command { cpu, _ ->
    cpu.displayState.scrollLeft(4)
}