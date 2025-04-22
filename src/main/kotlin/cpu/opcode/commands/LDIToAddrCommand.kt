package com.tomassirio.cpu.opcode.commands

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.opcode.Command

/**
    Annn - LD I, addr
    Set I = nnn.

    The value of register I is set to nnn.
*/

object LDIToAddrCommand : Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        val value = opcode and 0x0FFFu
        cpu.I.write(value)
    }
}
