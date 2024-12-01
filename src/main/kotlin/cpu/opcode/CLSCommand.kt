package com.tomassirio.cpu.opcode

import com.tomassirio.cpu.CPU

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