package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.Command

/**
 * 00EE - RET
 * Return from a subroutine.
 *
 * The interpreter sets the program counter to the address at the top of the stack, then subtracts 1 from the stack pointer.
 *
 */
fun retCommand(): Command {
    return Command(skipsPcIncrement = true) { cpu, _ ->
        cpu.pc.write(cpu.stack.pop())
    }
}