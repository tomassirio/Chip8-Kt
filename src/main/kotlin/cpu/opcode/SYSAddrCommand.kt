package com.tomassirio.cpu.opcode

import com.tomassirio.cpu.CPU

/**
 * 0nnn - SYS addr
 * Jump to a machine code routine at nnn.
 *
 * This instruction is only used on the old computers on which Chip-8 was originally implemented. It is ignored by modern interpreters.
 */
object SYSAddrCommand: Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        cpu.I.write(opcode.and(0xFFFu))
    }
}