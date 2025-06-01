package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.Command

/**
 * 1nnn - JP addr
 * Jump to location nnn.
 *
 * The interpreter sets the program counter to nnn.
 */
fun jpAddrCommand(): Command {
    return Command { cpu, opcode ->
        cpu.pc.write(opcode.and(0x0FFFu))
    }
}