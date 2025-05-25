package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.Command

/**
    Annn - LD I, addr
    Set I = nnn.

    The value of register I is set to nnn.
*/
fun ldIToAddrCommand(): Command {
    return Command {cpu, opcode ->
        val value = opcode and 0x0FFFu
        cpu.I.write(value)
    }
}