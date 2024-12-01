package com.tomassirio.cpu.opcode.commands

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.opcode.Command

/**
 * 2nnn - CALL addr
 * Call subroutine at nnn.
 *
 * The interpreter increments the stack pointer, then puts the current PC on the top of the stack. The PC is then set to nnn.
 */
object CALLAddrCommand : Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        cpu.sp.write(cpu.sp.read().plus(1u).toUByte())
        cpu.stack.push(cpu.pc.read())
        cpu.pc.write(opcode.and(0x0FFFu))
    }
}