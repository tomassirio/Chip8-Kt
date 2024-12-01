package com.tomassirio.cpu.opcode.commands

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.opcode.Command

/**
 * 00E0 - CLS
 * Clear the display.
 *
 */
object CLSCommand: Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        cpu.display.clearScreen()
    }
}