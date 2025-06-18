package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.opcode.Command

/**
 * 2nnn - CALL addr
 * Call subroutine at nnn.
 *
 * The interpreter increments the stack pointer, then puts the current PC on the top of the stack. The PC is then set to nnn.
 */
fun callAddrCommand(): Command {
    return Command(skipsPcIncrement = true) { cpu, opcode ->
        cpu.stack.push(cpu.pc.read().plus(2u).toUShort())
        cpu.pc.write(opcode.and(0x0FFFu))
    }
}