package com.tomassirio.cpu.opcode.commands

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.opcode.Command

/**
    Bnnn - JP V0, addr
    Jump to location nnn + V0.

    The program counter is set to nnn plus the value of V0.
*/

object JPAddrV0Command : Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        val v0Value = cpu.registers[0].read()
        val address = opcode and 0x0FFFu

        cpu.pc.write((address + v0Value.toUShort()).toUShort())
    }
}
