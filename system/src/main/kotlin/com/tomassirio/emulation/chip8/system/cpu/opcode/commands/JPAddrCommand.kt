package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.opcode.Command

/**
 * 1nnn - JP addr
 * Jump to location nnn.
 *
 * The interpreter sets the program counter to nnn.
 */
fun jpAddrCommand(): Command {
    return Command(skipsPcIncrement = true) { cpu, opcode ->
        cpu.pc.write(opcode.and(0x0FFFu))
    }
}