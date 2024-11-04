package com.tomassirio.cpu.opcode

import com.tomassirio.cpu.CPU

object SysAddrCommand: Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        cpu.I.write(opcode.and(0xFFFu))
    }
}