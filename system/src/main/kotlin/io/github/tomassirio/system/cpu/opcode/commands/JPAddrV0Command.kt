package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.cpu.opcode.Command

/**
Bnnn - JP V0, addr
Jump to location nnn + V0.

The program counter is set to nnn plus the value of V0.
 */
fun jpAddrV0Command(): Command {
    return Command(skipsPcIncrement = true) { cpu, opcode ->
        val v0Value = cpu.registers[0].read()
        val address = opcode and 0x0FFFu

        cpu.pc.write((address + v0Value.toUShort()).toUShort())
    }
}