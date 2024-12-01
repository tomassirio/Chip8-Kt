package com.tomassirio.cpu.opcode

import com.tomassirio.cpu.CPU

object CLSCommand: Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        cpu.display.clearScreen();
    }
}