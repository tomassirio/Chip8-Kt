package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.opcode.Command

/**
 * 1nnn - JP addr
 * Jump to location nnn.
 *
 * The interpreter sets the program counter to nnn.
 */
object JPAddrCommand : Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        cpu.pc.write(opcode.and(0x0FFFu))
    }
}