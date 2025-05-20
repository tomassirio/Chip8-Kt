package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.opcode.Command

/**
 * 00E0 - CLS
 * Clear the display.
 *
 */
object CLSCommand: Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        cpu.displayState.clearScreen()
    }
}